package com.friendship41.vodserver.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("FileService")
public class FileServiceImpl implements FileService {
  private static final Log LOG = LogFactory.getLog(FileServiceImpl.class);

  @Autowired
  private ConvertService convertService;

  @Value("${media_dir}")
  private String mediaDir;

  @Override
  public File getFile(final String fileName) {
    System.out.println("fileName: "+fileName);
    return new File(mediaDir+convertService.decodeUrlUTF8ToString(fileName));
  }

  @Override
  public Map<Map<String, String>, String> getDirAndFileMap(final String pathNow) {
    System.out.println(pathNow);
    String path;
    if (pathNow == null) {
      path = "";
    } else {
      path = convertService.decodeUrlUTF8ToString(pathNow);
    }
    String resultPath = path;
    path = mediaDir+path;
    System.out.println(path);
    File dir = new File(path);
    File[] fileArr = dir.listFiles();

    Map<Map<String, String>, String> map = new HashMap<>();
    for (File file : fileArr) {
      Map<String, String> fileMap = new HashMap<>();
      System.out.println(file.getName());
      fileMap.put(file.getName(), convertService.encodeStringToUrlUTF8(resultPath+"/"+file.getName()));
      if (file.isFile()) {
        map.put(fileMap, "file");
      } else if (file.isDirectory()) {
        map.put(fileMap, "dir");
      }
    }
    System.out.println("final: "+map);
    return map;
  }

  @Override
  public void playFile(final File file, final HttpServletRequest request, HttpServletResponse response)
      throws FileNotFoundException {
    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");

    long rangeStart = 0; //요청 범위의 시작 위치
    long rangeEnd = 0; //요청 범위의 끝 위치
    boolean isPart=false; //부분 요청일 경우 true, 전체 요청의 경우 false

    try {
      long vodSize = randomAccessFile.length();
      String range = request.getHeader("range");

      if (range != null) {
        if (range.endsWith("-")) {
          range =range+(vodSize-1);
        }
        int idxm = range.trim().indexOf("-");
        rangeStart = Long.parseLong(range.substring(6, idxm));
        rangeEnd = Long.parseLong(range.substring(idxm+1));
        if (rangeStart > 0) {
          isPart =true;
        }
      } else {
        rangeStart = 0;
        rangeEnd = vodSize - 1;
      }

      long partSize = rangeEnd - rangeStart + 1;

      response.reset();

      response.setStatus(isPart ? 206 : 200);

      String[] temp = file.getName().split("\\.");
      response.setContentType("video/"+temp[temp.length-1]);

      response.setHeader("Content-Range", "bytes "+rangeStart+"-"+rangeEnd+"/"+vodSize);
      response.setHeader("Accept-Ranges", "bytes");
      response.setHeader("Content-Length", ""+partSize);

      OutputStream outputStream = response.getOutputStream();

      randomAccessFile.seek(rangeStart);

      int bufferSize = 8*1024;
      byte[] buff = new byte[bufferSize];
      do {
        int block = partSize > bufferSize ? bufferSize : (int) partSize;
        int len = randomAccessFile.read(buff, 0,
            block);
        outputStream.write(buff, 0, len);
        partSize -= block;
      } while (partSize > 0);

    } catch (IOException ignored) {
    } finally {
      try {
        randomAccessFile.close();
      } catch (IOException ignored) {
      }
    }


  }
}

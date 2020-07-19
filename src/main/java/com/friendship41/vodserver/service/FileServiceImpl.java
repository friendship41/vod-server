package com.friendship41.vodserver.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.annotation.PostConstruct;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Utf8;
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
}

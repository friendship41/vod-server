package com.friendship41.vodserver.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Service;

@Service("FileService")
public class FileServiceImpl implements FileService {
  private static final Log LOG = LogFactory.getLog(FileServiceImpl.class);

  @Value("${media_dir}")
  private String mediaDir;

  @Override
  public File getFile(final String fileName) {
    System.out.println("fileName: "+fileName);
    System.out.println(fileName.replaceAll("-_q", "/"));
    return new File(mediaDir+fileName.replaceAll("-_q", "/"));
  }

  @Override
  public Map<String, String> getDirAndFileMap(final String pathNow) throws UnsupportedEncodingException {
    System.out.println(pathNow);
    String path = "";
    if (pathNow == null) {
      path = "";
    } else {
      path = pathNow.replaceAll("-_q", "/");
    }
    path = mediaDir+path;
    System.out.println(path);
    File dir = new File(path);
    File[] fileArr = dir.listFiles();

    Map<String, String> map = new HashMap<>();
    for (File file : fileArr) {
      System.out.println(file.getName());
      if (file.isFile()) {
        map.put(URLEncoder.encode(file.getName(), "UTF-8"), "file");
      } else if (file.isDirectory()) {
        map.put(URLEncoder.encode(file.getName(), "UTF-8"), "dir");
      }
    }
    System.out.println(map);
    return map;
  }
}

package com.friendship41.vodserver.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FileService {
  File getFile(String fileName);
  Map<Map<String, String>, String> getDirAndFileMap(String pathNow) throws UnsupportedEncodingException;
  void playFile(File file, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException;
}

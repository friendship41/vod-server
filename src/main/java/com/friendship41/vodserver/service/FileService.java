package com.friendship41.vodserver.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public interface FileService {
  File getFile(String fileName);
  Map<Map<String, String>, String> getDirAndFileMap(String pathNow) throws UnsupportedEncodingException;
}

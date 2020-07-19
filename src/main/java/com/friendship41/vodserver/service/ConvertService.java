package com.friendship41.vodserver.service;

public interface ConvertService {
  String encodeStringToUrlUTF8(String originalStr);
  String decodeUrlUTF8ToString(String encodedStr);
}

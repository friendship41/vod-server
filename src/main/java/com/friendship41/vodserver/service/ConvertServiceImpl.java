package com.friendship41.vodserver.service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service("ConvertService")
public class ConvertServiceImpl implements ConvertService {
  private static final Log LOG = LogFactory.getLog(ConvertServiceImpl.class);

  @Override
  public String encodeStringToUrlUTF8(final String originalStr) {
    try {
      return URLEncoder.encode(originalStr, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      LOG.error(e);
      return originalStr;
    }
  }

  @Override
  public String decodeUrlUTF8ToString(final String encodedStr) {
    try {
      return URLDecoder.decode(encodedStr, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      LOG.error(e);
      return encodedStr;
    }
  }
}

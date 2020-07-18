package com.friendship41.vodserver.controller;

import com.friendship41.vodserver.service.FileService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
public class VodController {
  @Autowired
  private FileService fileService;

  @RequestMapping(value = "/test")
  public String testController() {
    return "index";
  }

  @RequestMapping(value = "/index/{pathNow}")
  public String vodIndex(final Model model, @PathVariable("pathNow") String pathNow)
      throws UnsupportedEncodingException {
    if (pathNow.equals("~qwe")) {
      pathNow="";
      model.addAttribute("path", "");
    } else {
      model.addAttribute("path", pathNow+"-_q");
    }
    model.addAttribute("map", fileService.getDirAndFileMap(pathNow));
    return "vod-index";
  }

  @RequestMapping(value = "/vod/{fileName}")
  public String vodPage(@PathVariable("fileName") String fileName, Model model) {
    model.addAttribute("fileName", fileName);
    return "vod";
  }

  @RequestMapping(value = "/play/{fileName}")
  public StreamingResponseBody play(@PathVariable("fileName") final String fileName) throws FileNotFoundException {
    final InputStream inputStream = new FileInputStream(fileService.getFile(fileName));
    return outputStream -> {
      readAndWrite(inputStream, outputStream);
    };
  }

  private void readAndWrite(final InputStream inputStream, final OutputStream outputStream) throws IOException {
    byte[] data = new byte[8048];
    int read = 0;
    while ((read = inputStream.read(data)) > 0) {
      outputStream.write(data, 0, read);
    }
    outputStream.flush();
    inputStream.close();
    outputStream.close();
  }
}

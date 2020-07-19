package com.friendship41.vodserver.controller;

import com.friendship41.vodserver.data.ReqFile;
import com.friendship41.vodserver.service.ConvertService;
import com.friendship41.vodserver.service.FileService;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
public class VodController {
  @Autowired
  private FileService fileService;
  @Autowired
  private ConvertService convertService;

  @RequestMapping(value = "/test")
  public String testController() {
    return "index";
  }

  @RequestMapping(value = "/")
  public String mainIndex() {
    return "redirect:/index?pathNow=~qwe";
  }

  @RequestMapping(value = "/index")
  public String vodIndex(final Model model, HttpServletRequest request)
      throws UnsupportedEncodingException {
    System.out.println("====================================");
    String pathNow = request.getParameter("pathNow");
    System.out.println(pathNow);
    if (pathNow.equals("~qwe")) {
      pathNow = "";
    }
    model.addAttribute("map", fileService.getDirAndFileMap(pathNow));
    return "vod-index";
  }

  @RequestMapping(value = "/vod")
  public String vodPage(HttpServletRequest request, Model model) {
    System.out.println("====================================");
    System.out.println("/vod");
    String fileName = request.getParameter("fileName");
    fileName = convertService.decodeUrlUTF8ToString(fileName);
    System.out.println(fileName);
    if (fileName.charAt(0) == '/') {
      fileName = fileName.substring(1);
    }
    fileName = fileName.replaceAll("/", "-qwe-");
    fileName = convertService.encodeStringToUrlUTF8(fileName);
    model.addAttribute("fileName", fileName);
    return "vod";
  }

  @RequestMapping(value = "/play/{fileName}")
  public StreamingResponseBody play(@PathVariable("fileName") String fileName) throws FileNotFoundException {
    System.out.println("====================================");
    System.out.println("/play");
    fileName = fileName.replaceAll("-qwe-", "/");
    System.out.println(convertService.decodeUrlUTF8ToString(fileName));
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
  }
}

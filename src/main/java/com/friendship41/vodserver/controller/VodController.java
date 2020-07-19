package com.friendship41.vodserver.controller;

import com.friendship41.vodserver.service.ConvertService;
import com.friendship41.vodserver.service.FileService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class VodController {
  private static final Log LOG = LogFactory.getLog(VodController.class);

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
  public String play(@PathVariable("fileName") String fileName, HttpServletRequest request, HttpServletResponse response)
      throws FileNotFoundException {
    System.out.println("====================================");
    System.out.println("/play");
    fileName = fileName.replaceAll("-qwe-", "/");
    System.out.println(convertService.decodeUrlUTF8ToString(fileName));
    File file = fileService.getFile(fileName);

    fileService.playFile(file, request, response);

    return null;
  }
}

package com.preproject.seb_pre_15.image;

import com.preproject.seb_pre_15.argumentresolver.LoginMemberId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Positive;
import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {
  @Autowired
  private ImageService imageService;
  
  @PostMapping
  public ResponseEntity<String> postImage(@RequestParam("file") MultipartFile file,
                                          @LoginMemberId Long memberId) {
    try {
      imageService.saveImage(file, memberId);
      return ResponseEntity.status(HttpStatus.CREATED).body("Image uploaded successfully");
    } catch (IOException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
    }
  }

  @PatchMapping("/{image-id}")
  public ResponseEntity updateImage(@RequestParam("file") MultipartFile file,
                                    @PathVariable("image-id") @Positive Long imageId,
                                    @LoginMemberId Long memberId) throws IOException {
    imageService.updateImage(file , imageId, memberId);
    return ResponseEntity.status(HttpStatus.OK).body("Image update successfully");
  }
  
  @GetMapping("/{image-id}")
  public ResponseEntity<byte[]> getImage(@PathVariable("image-id") @Positive Long imageId) {
    byte[] imageData = imageService.getImage(imageId);
    return new ResponseEntity<>(imageData, HttpStatus.OK);
  }
  
  @DeleteMapping("/{image-id}")
  public ResponseEntity deleteImage(@PathVariable("image-id") @Positive Long imageId,
                                    @LoginMemberId Long memberId){
    imageService.deleteImage(imageId, memberId);
    return new ResponseEntity<>("success delete image", HttpStatus.NO_CONTENT);
  }
}

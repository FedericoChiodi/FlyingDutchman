package com.ingsw.flyingdutchman.services;

import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Controller
public class UploadController {

    private final UserService userService;

    @Autowired
    public UploadController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/Upload")
    public String handleFileUpload(
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile file,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) throws IOException {

        User loggedUser = userService.findLoggedUser(request);

        // Specifica il percorso della cartella in cui desideri salvare il file
        String uploadDirPath = "/home/sanpc/Uploads/" + loggedUser.getUsername();

        // Crea la cartella se non esiste
        File uploadDir = new File(uploadDirPath);
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                throw new IOException("Directory could not be created.");
            }
        }

        // Crea il percorso completo del file di destinazione
        String fileExtension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.(?=[^.]+$)")[1];
        String filePath = uploadDirPath + File.separator + description + "." + fileExtension;

        // Salva il file nel percorso specificato
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");
        return "productManagement/view";
    }
}

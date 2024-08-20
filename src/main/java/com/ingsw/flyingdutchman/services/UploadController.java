package com.ingsw.flyingdutchman.services;

import com.ingsw.flyingdutchman.model.mo.Category;
import com.ingsw.flyingdutchman.model.mo.Product;
import com.ingsw.flyingdutchman.model.mo.User;
import com.ingsw.flyingdutchman.model.service.CategoryService;
import com.ingsw.flyingdutchman.model.service.ProductService;
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
import java.util.List;
import java.util.Objects;

@Controller
public class UploadController {

    private final UserService userService;

    private final ProductService productService;

    private final CategoryService categoryService;

    @Autowired
    public UploadController(UserService userService, ProductService productService, CategoryService categoryService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @PostMapping("/Upload")
    public String handleFileUpload(
            @RequestParam("description") String description,
            @RequestParam("image") MultipartFile file,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) throws IOException {

        User loggedUser = userService.findLoggedUser(request);

        // Crea la cartella se non esiste
        File uploadDir = getUploadDirectory(loggedUser.getUsername());
        if (!uploadDir.exists()) {
            if (!uploadDir.mkdirs()) {
                throw new IOException("Directory could not be created.");
            }
        }

        // Crea il percorso completo del file di destinazione
        String fileExtension = Objects.requireNonNull(file.getOriginalFilename()).split("\\.(?=[^.]+$)")[1];
        String filePath = uploadDir.getAbsolutePath() + File.separator + description + "." + fileExtension;

        // Salva il file nel percorso specificato
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        redirectAttributes.addFlashAttribute("message", "File uploaded successfully!");

        try{
            productService.createProduct(
                    description,
                    Float.valueOf(request.getParameter("min_price")),
                    Float.valueOf(request.getParameter("starting_price")),
                    Float.valueOf(request.getParameter("starting_price")),
                    filePath,
                    categoryService.findCategoryById(Long.valueOf(request.getParameter("categoryID"))),
                    loggedUser
            );
        }
        catch (Exception e){
            request.setAttribute("applicationMessage", "Could not create product!");
        }

        List<Product> products = productService.findProductByOwnerNotDeletedNotSold(loggedUser);

        request.setAttribute("loggedOn", true);
        request.setAttribute("loggedUser", loggedUser);
        request.setAttribute("products", products);
        request.setAttribute("soldProductsAction", false);
        request.setAttribute("menuActiveLink", "Prodotti");
        request.setAttribute("loggedOn", true);
        request.setAttribute("loggedUser", loggedUser);

        return "productManagement/view";
    }

    protected File getUploadDirectory(String username) {
        return new File("/srv/flyingdutchman/uploads/" + username);
    }
}
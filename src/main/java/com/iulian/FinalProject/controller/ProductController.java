package com.iulian.FinalProject.controller;

import com.iulian.FinalProject.model.Product;
import com.iulian.FinalProject.model.User;
import com.iulian.FinalProject.repository.ProductRepository;
import com.iulian.FinalProject.repository.UserRepository;
import com.iulian.FinalProject.security.UserDetailsPrincipal;
import com.iulian.FinalProject.service.ProductService;
import com.iulian.FinalProject.util.FileUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ProductController {

    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductService productService;
    @Autowired
    UserRepository userRepository;

    @GetMapping(value = "/product")
    public String viewProduct(@RequestParam(name = "id") int productId, @AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model) {
        Product product = new Product();
        product = productRepository.findById(productId);
        model.addAttribute("product", product);

        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            User user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }

        return "productPage";
    }
    // Lists all products
    @GetMapping(value = "/adminPanel/products")
    public String products_panel(@AuthenticationPrincipal UserDetailsPrincipal userDetailsPrincipal, Model model) {
        if(userDetailsPrincipal != null) {
            String userEmail = userDetailsPrincipal.getEmail();
            User user = userRepository.findByEmail(userEmail);
            model.addAttribute("currentLoggedUser", user);
        }
        model.addAttribute("products", productRepository.findAll());
        return "productsPanel";
    }

    // Get product by id
    @GetMapping(value = "/adminPanel/product/{id}")
    public String edit_product_form(@PathVariable(name = "id") int productId, Model model) throws IOException {
        model.addAttribute("product", productRepository.findById(productId));
        return "updateProductPage";
    }

    // Create a new product button
    @PostMapping(value = "/adminPanel/product/create_product")
    public String create_product(@RequestParam MultipartFile multipartFile, HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        if(!productService.productExists(request.getParameter("uid"))) {
            Product product = new Product();

            product.setTitle(request.getParameter("title"));
            product.setDescription(request.getParameter("description"));
            product.setPrice(Integer.parseInt(request.getParameter("price")));
            product.setStock(Integer.parseInt(request.getParameter("stock")));
            product.setUid(request.getParameter("uid"));

            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            product.setImage(fileName);

            productService.createProduct(product);

            String uploadDir = "src/main/resources/static/images/productImages/" + product.getId();

            FileUtil.saveFile(uploadDir, fileName, multipartFile);

            redirectAttributes.addFlashAttribute("successMessage", "Successfully created product: " + product.getUid() + ".");
        } else {
            redirectAttributes.addFlashAttribute("message", "A product with this UID already exists!");
        }
        return "redirect:/adminPanel/products";
    }


    // Called after submitting the update product form, saving the product details
    @PostMapping(value = "/adminPanel/product/save/{id}")
    public String saveProductForm(@RequestParam MultipartFile multipartFile, @PathVariable(name = "id") int productId, @ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) throws IOException {
        Product oldProduct = productRepository.findById(productId);

        if(!productService.productExists(product.getId()) || oldProduct.getId() == product.getId()) {
            oldProduct.setTitle(product.getTitle());
            oldProduct.setDescription(product.getDescription());
            oldProduct.setPrice(product.getPrice());
            oldProduct.setStock(product.getStock());
            oldProduct.setUid(product.getUid());

            if(oldProduct.getUid() != product.getUid()) {
                // Deleting the previous product image
                String fileDir = "src/main/resources/static/images/productImages/" + oldProduct.getId() + "/" + oldProduct.getImage();
                FileUtil.deleteFile(fileDir);

                // Setting the new product image
                String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
                oldProduct.setImage(fileName);

                // Getting the correct directory and saving the image.
                String uploadDir = "src/main/resources/static/images/productImages/" + oldProduct.getId();
                FileUtil.saveFile(uploadDir, fileName, multipartFile);
            }

            productRepository.save(oldProduct);

            redirectAttributes.addFlashAttribute("successMessage", "Successfully updated product: " + oldProduct.getUid() + ".");
        } else if(productService.productExists(product.getId())) {
            redirectAttributes.addFlashAttribute("message", "A product with this UID already exists!");
        }
        return "redirect:/adminPanel/products";
    }

    // Delete product by id
    @GetMapping(value = "/adminPanel/product/delete_product")
    public String delete_Product(@RequestParam(name = "id") int productId, RedirectAttributes redirectAttributes) throws IOException {
        if(productService.productExists(productId)) {
            redirectAttributes.addFlashAttribute("message", "Successfully deleted product: " + productRepository.findById(productId).getTitle() + ".");

            // Getting the folder id and
            String fileDir = "src/main/resources/static/images/productImages/" + productId;
            FileUtil.deleteDirectory(fileDir);
            productRepository.deleteById(productId);
        } else {
            redirectAttributes.addFlashAttribute("message", "Cannot delete product. Product does not exist.");
        }
        return "redirect:/adminPanel/products";
    }

}

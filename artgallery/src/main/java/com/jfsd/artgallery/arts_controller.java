package com.jfsd.artgallery;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.hibernate.annotations.DialectOverride.OverridesAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.ServletContextAware;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import javax.servlet.ServletContext;
import org.springframework.web.context.ServletContextAware;


@Controller
public class arts_controller implements ServletContextAware {
	 
	@Autowired
	private ArtRepository repo;
	private ServletContext servletContext;
 
	public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        System.out.println("ServletContext set: " + servletContext); 
    }
	
  
	@GetMapping("")
	public String showart(Model model) {
		model.addAttribute("arts", new art());
		return "index.html";
	
		
	}
	@PostMapping("/process_register")
	public String showartses(art artsi) {
		repo.save(artsi);
		return "signup";
	}
	@GetMapping("/artss")
	public String showartss() {
		return "artss";
	}
	
		
	@GetMapping("/artists")
	public String showartsi(Model model) {
		return "artists";
	} 
	

	@GetMapping("/createart")
	public String showartsis(Model model){
		model.addAttribute("arts", new art());
        return "createart";  
    }
	
	@PostMapping("/create-user")
	public String showartsesj(art artsi) {
		repo.save(artsi);
		return "createres";
	}

    
  

    @GetMapping("/editart")
    public String showartsit(Model model) {
    	model.addAttribute("arts", new art());
        return "editart";  
    }
    
    @PostMapping("/update-user")
    public String updateUser(@ModelAttribute("arts") art arts, Model model) {
		String email = arts.getEmail();
		String password = arts.getPassword();

		art existingUser = repo.findByEmailAndPassword(email, password);
        if (existingUser != null) {
            
            int updatedRows = repo.updateUserDetails(email, arts.getEmail(), arts.getPassword());
            
            if (updatedRows > 0) {
                model.addAttribute("message", "Account updated successfully.");
            } else {
                model.addAttribute("error", "Failed to update account.");
            }
        } else {
            model.addAttribute("error", "Invalid email or password.");
        }

        return "editres";
    }

    
    @GetMapping("/deleteart")
    public String showartsie(Model model) {
    	model.addAttribute("arts", new art());
        return "deleteart";  
    }
    @PostMapping("/delete-user")
    public String deleteUser(@ModelAttribute("arts") art arts, Model model) {
        
        String email = arts.getEmail();
        String password = arts.getPassword();

        
        art userToDelete = repo.findByEmailAndPassword(  email,  password);
        if (userToDelete != null) {
            
            repo.delete(userToDelete);
            model.addAttribute("message", "Account deleted successfully.");
        } else {
            
            model.addAttribute("error", "Invalid email or password.");
        }

        return "deleteres";
    }  
    @GetMapping("imageinse")
    public String showartsiet(Model model) {
    	model.addAttribute("arts", new art());
        return "imageinse";  
    }
    
   
    @PostMapping("/upload-image")
    public String uploadImage(@RequestParam("image") MultipartFile file, Model model) {
        try {
            // Get the real path to the "uploads" folder inside the static directory
            String uploadDir = servletContext.getRealPath("/static/uploads/");

            // Ensure the upload directory exists
            Path path = Paths.get(uploadDir);
            if (!Files.exists(path)) {
                Files.createDirectories(path); // Create the directory if it doesn't exist
            }

            // Save the file
            String fileName = file.getOriginalFilename();
            if (fileName != null && !fileName.isEmpty()) {
                // Resolve the file path inside the uploads folder
                Path filePath = path.resolve(fileName);
                file.transferTo(filePath.toFile()); // Transfer the uploaded file to the server

                // Add the uploaded file's path to the model to display it
                model.addAttribute("uploadedFilePath", "/uploads/" + fileName);
            } else {
                model.addAttribute("error", "Invalid file name!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error uploading file: " + e.getMessage());
            return "error"; // Ensure an `error.html` template exists to handle errors
        }

        return "userimg"; // Redirect to the page where the uploaded image is displayed
    }

    // View uploaded images
    @GetMapping("/userimg")
    public String viewUploadedImages(Model model) {
        // Get the path to the "uploads" folder inside the static directory
        String uploadDir = servletContext.getRealPath("/static/uploads/");
        File folder = new File(uploadDir);

        // List all image files
        String[] images = folder.list((dir, name) -> name.matches(".*\\.(png|jpg|jpeg|gif)"));
        if (images != null) {
            model.addAttribute("uploadedImages", images);
        }

        return "userimg";  // Return the view that will display images
    }


	@Override
	public void setServletContext(jakarta.servlet.ServletContext servletContext) {
		// TODO Auto-generated method stub
		
	}
  

 


}

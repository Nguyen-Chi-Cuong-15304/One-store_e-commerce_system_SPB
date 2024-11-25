package com.example.Project1.controller.Supplier;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.Project1.entity.Supplier;
import com.example.Project1.modals.SupplierDto;
import com.example.Project1.repository.SupplierRepository;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/supplier")
public class SupplierActionController {
    @Autowired
    private SupplierRepository supplierRepository;
    @GetMapping({"/", ""})
    public String show_supplier(Model model) {
        List<Supplier> suppliers = supplierRepository.findAll();
        model.addAttribute("suppliers", suppliers);
        return "admin/adminDashboardSupplier";
    }
    
    @GetMapping("/add")
    public String add(Model model) {
        SupplierDto supplierDto = new SupplierDto();
        model.addAttribute("supplierDto", supplierDto);
        return "supplier/addSupplier";
    }
    @PostMapping("/add")
    public String addSupplier(Model model, @ModelAttribute("supplierDto") SupplierDto supplierDto, BindingResult bindingResult) {
        if(supplierDto.getSupplierName().isEmpty()){
            bindingResult.addError(new FieldError("supplierDto", "supplierName", "Supplier name is required"));
        }
        if(supplierDto.getLinkImg().isEmpty()){
            bindingResult.addError(new FieldError("supplierDto", "linkImg", "Link image is required"));
        }
        if(supplierDto.getAddress().isEmpty()){
            bindingResult.addError(new FieldError("supplierDto", "address", "Address is required"));
        }
        if(supplierDto.getPhoneNumber().isEmpty()){
            bindingResult.addError(new FieldError("supplierDto", "phoneNumber", "Phone number is required"));
        }
        if (supplierDto.getEmail().isEmpty()) {
            bindingResult.addError(new FieldError("supplierDto", "email", "Email is required"));
        }
        if (supplierDto.getWebsite().isEmpty()) {
            bindingResult.addError(new FieldError("supplierDto", "website", "Website is required"));
        }
        if (supplierDto.getDescription().isEmpty()) {
            bindingResult.addError(new FieldError("supplierDto", "description", "Description is required"));
        }
        if(bindingResult.hasErrors()){
            return "supplier/addSupplier";
        }
        MultipartFile file = supplierDto.getLinkImg();
        Date createTime = new Date(System.currentTimeMillis());
        String fileName = createTime.getTime() + file.getOriginalFilename();
        try {
            String uploadDir = "public/supplier_image/";
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, Paths.get(uploadDir, fileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception exception) {
            System.out.println("Exception: " + exception.getMessage());
        }
        Supplier supplier = new Supplier();
        supplier.setSupplierName(supplierDto.getSupplierName());
        supplier.setAddress(supplierDto.getAddress());
        supplier.setPhoneNumber(supplierDto.getPhoneNumber());
        supplier.setEmail(supplierDto.getEmail());
        supplier.setWebsite(supplierDto.getWebsite());
        supplier.setLinkImg(fileName);
        supplier.setDescription(supplierDto.getDescription());
        supplierRepository.save(supplier);
        return "redirect:/supplier";
    }
    @GetMapping("/edit")
    public String edit(@RequestParam("id") int id, Model model) {
        Supplier supplier = supplierRepository.findById(id).get();
        model.addAttribute("supplier", supplier);
        SupplierDto supplierDto = new SupplierDto();
        supplierDto.setSupplierName(supplier.getSupplierName());
        supplierDto.setAddress(supplier.getAddress());
        supplierDto.setPhoneNumber(supplier.getPhoneNumber());
        supplierDto.setEmail(supplier.getEmail());
        supplierDto.setWebsite(supplier.getWebsite());
        supplierDto.setLinkImg(null);
        supplierDto.setDescription(supplier.getDescription());
        model.addAttribute("supplierDto", supplierDto);
        return "supplier/edit_supplier";
    }

    @PostMapping("/edit")
    public String editSupplier(Model model, @RequestParam int id, @Valid @ModelAttribute SupplierDto supplierDto, BindingResult bindingResult) {
        try {
            Supplier supplier = supplierRepository.findById(id).get();
            model.addAttribute("supplier", supplier);
            if(supplierDto.getSupplierName().isEmpty()){
                bindingResult.addError(new FieldError("supplierDto", "supplierName", "Supplier name is required"));
            }
            if(supplierDto.getAddress().isEmpty()){
                bindingResult.addError(new FieldError("supplierDto", "address", "Address is required"));
            }
            if(supplierDto.getPhoneNumber().isEmpty()){
                bindingResult.addError(new FieldError("supplierDto", "phoneNumber", "Phone number is required"));
            }
            if (supplierDto.getEmail().isEmpty()) {
                bindingResult.addError(new FieldError("supplierDto", "email", "Email is required"));
            }
            if (supplierDto.getWebsite().isEmpty()) {
                bindingResult.addError(new FieldError("supplierDto", "website", "Website is required"));
            }
            if (supplierDto.getDescription().isEmpty()) {
                bindingResult.addError(new FieldError("supplierDto", "description", "Description is required"));
            }
            if(bindingResult.hasErrors()){
                return "supplier/edit_supplier";
            }
            if(!supplierDto.getLinkImg().isEmpty()){
                String uploadDir = "public/supplier_image/";
                Path oldPath = Paths.get(uploadDir + supplier.getLinkImg());
                try{
                    Files.delete(oldPath);
                }
                catch (Exception e){
                    System.out.println("Exception: " + e.getMessage());
                }
                MultipartFile file = supplierDto.getLinkImg();
                Date createTime = new Date(System.currentTimeMillis());
                String fileName = createTime.getTime() + file.getOriginalFilename();
                try(InputStream inputStream = file.getInputStream()){
                    Files.copy(inputStream, Paths.get(uploadDir, fileName), StandardCopyOption.REPLACE_EXISTING);
                }
                catch (Exception exception) {
                    System.out.println("Exception: " + exception.getMessage());
                }
                supplier.setLinkImg(fileName);
            }
            supplier.setSupplierName(supplierDto.getSupplierName());
            supplier.setAddress(supplierDto.getAddress());
            supplier.setPhoneNumber(supplierDto.getPhoneNumber());
            supplier.setEmail(supplierDto.getEmail());
            supplier.setWebsite(supplierDto.getWebsite());
            supplier.setDescription(supplierDto.getDescription());
            supplierRepository.save(supplier);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/supplier";
    }
    @GetMapping("/delete")
    public String delete(@RequestParam int id) {
        Supplier supplier = supplierRepository.findById(id).get();
        String uploadDir = "public/supplier_image/";
        Path oldPath = Paths.get(uploadDir + supplier.getLinkImg());
        try{
            Files.delete(oldPath);
        }
        catch (Exception e){
            System.out.println("Exception: " + e.getMessage());
        }
        supplierRepository.deleteById(id);
        return "redirect:/supplier";   
    }
    
    
}

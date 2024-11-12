package com.example.Project1.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.Project1.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    public Product findByproductName(String productName);

    List<Product> findByProductNameContainingIgnoreCase(String productName);
    
    @Query("SELECT p FROM Product p Order by p.viewCount DESC")
    List<Product> findTopViewProducts(PageRequest pageRequest);

    public Product findByProductID(int productID);
    //Sản phẩm xếp không theo danh mục
    //Sản phẩm mới nhất
    @Query(value = "SELECT * FROM Product ORDER BY productID DESC", nativeQuery = true)
    List<Product> findByOrderByProductIDDesc();
    //Sản phẩm có giá giảm dần
    @Query(value = "SELECT * FROM Product ORDER BY price DESC", nativeQuery = true)
    List<Product> findByOrderByPriceDesc();
    //Sản phẩm có giá tăng dần
    @Query(value = "SELECT * FROM Product ORDER BY price ASC", nativeQuery = true)
    List<Product> findByOrderByPriceAsc();
    //Sản phẩm có lượt xem giảm dần
    @Query(value = "SELECT * FROM Product ORDER BY viewCount DESC", nativeQuery = true)
    List<Product> findByOrderByViewCountDesc();






    //Sản phẩm theo danh mục
    // default
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Category pc ON p.productID = pc.productID " +
                   "JOIN Category c ON c.categoryID = pc.categoryID " +
                   "WHERE c.categoryID = :categoryID ", nativeQuery = true)
    List<Product> findByCategoryID(@Param("categoryID") int categoryID);
    //Sản phẩm mới nhất
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Category pc ON p.productID = pc.productID " +
                   "JOIN Category c ON c.categoryID = pc.categoryID " +
                   "WHERE c.categoryID = :categoryID " +
                   "ORDER BY p.productID DESC", nativeQuery = true)
    List<Product> findByCategoryIDOrderByProductIDDesc(@Param("categoryID") int categoryID);
    //Sản phẩm có giá giảm dần
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Category pc ON p.productID = pc.productID " +
                   "JOIN Category c ON c.categoryID = pc.categoryID " +
                   "WHERE c.categoryID = :categoryID " +
                   "ORDER BY p.price DESC", nativeQuery = true)
    List<Product> findByCategoryIDOrderByPriceDesc(@Param("categoryID") int categoryID);
    //Sản phẩm có giá tăng dần 
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Category pc ON p.productID = pc.productID " +
                   "JOIN Category c ON c.categoryID = pc.categoryID " +
                   "WHERE c.categoryID = :categoryID " +
                   "ORDER BY p.price ASC", nativeQuery = true)
    List<Product> findByCategoryIDOrderByPriceAsc(@Param("categoryID") int categoryID);

    //Sản phẩm có lượt xem giảm dần
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Category pc ON p.productID = pc.productID " +
                   "JOIN Category c ON c.categoryID = pc.categoryID " +
                   "WHERE c.categoryID = :categoryID " +
                   "ORDER BY p.viewCount DESC", nativeQuery = true)
    List<Product> findByCategoryIDOrderByViewCountDesc(@Param("categoryID") int categoryID);


    //Sản phẩm theo vùng miền
    
    // Tìm theo id miền
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Region pr ON p.productID = pr.productID " +
                   "JOIN Region r ON r.regionID = pr.regionID " +
                   "WHERE r.regionID = :regionID ", nativeQuery = true)
    List<Product> findByRegionID(@Param("regionID") int regionID);

    //Sản phẩm mới nhất
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Region pr ON p.productID = pr.productID " +
                   "JOIN Region r ON r.regionID = pr.regionID " +
                   "WHERE r.regionID = :regionID " +
                   "ORDER BY p.productID DESC", nativeQuery = true)
    List<Product> findByRegionIDOrderByProductIDDesc(@Param("regionID") int regionID);
    //Sản phẩm có giá giảm dần
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Region pr ON p.productID = pr.productID " +
                   "JOIN Region r ON r.regionID = pr.regionID " +
                   "WHERE r.regionID = :regionID " +
                   "ORDER BY p.price DESC", nativeQuery = true)
    List<Product> findByRegionIDOrderByPriceDesc(@Param("regionID") int regionID);
    //Sản phẩm có giá tăng dần
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Region pr ON p.productID = pr.productID " +
                   "JOIN Region r ON r.regionID = pr.regionID " +
                   "WHERE r.regionID = :regionID " +
                   "ORDER BY p.price ASC", nativeQuery = true)
    List<Product> findByRegionIDOrderByPriceAsc(@Param("regionID") int regionID);

    //Sản phẩm có lượt xem giảm dần
    @Query(value = "SELECT p.* FROM Product p " +
                   "JOIN Product_Region pr ON p.productID = pr.productID " +
                   "JOIN Region r ON r.regionID = pr.regionID " +
                   "WHERE r.regionID = :regionID " +
                   "ORDER BY p.viewCount DESC", nativeQuery = true)
    List<Product> findByRegionIDOrderByViewCountDesc(@Param("regionID") int regionID);
    


}
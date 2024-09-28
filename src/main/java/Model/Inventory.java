package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();


    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }


    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }


    public static boolean deletePart(Part selectedPart){
        allParts.remove(selectedPart);
        return true;
    }


    public static boolean deleteProduct(Product selectedProduct){
        allProducts.remove(selectedProduct);
        return true;
    }


    public static ObservableList<Part> getAllParts() {
        return allParts;
    }


    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    static{
        initialDataSet();
    }
    public static void initialDataSet(){

        Part part1 = new InHouse(1, "Window", 15.00, 13, 1, 20, 110);
        Part part2 = new InHouse(2, "Wheel", 17.00, 15, 1, 20, 111);
        Part part3 = new Outsourced(3, "Seat", 17.00, 13, 1, 20, "CarParts.com");

        Product product1 = new Product(100, "Car", 299.99, 5, 1, 20);
        Product product2 = new Product(101, "Giant Bike", 299.99, 5, 1, 20);
        Product product3 = new Product(103, "little Bike", 199.99, 3, 1, 20);

        //add data to table
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);

        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
    }

}

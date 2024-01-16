package br.com.guilchaves.dscatalog.util;

import br.com.guilchaves.dscatalog.entities.Product;
import br.com.guilchaves.dscatalog.projections.ProductProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {


    public static List<Product> replace(List<ProductProjection> ordered, List<Product> unordered) {
        Map<Long, Product> map = new HashMap<>();
        unordered.forEach(obj -> map.put(obj.getId(), obj));

        List<Product> result = new ArrayList<>();
        ordered.forEach(obj -> result.add(map.get(obj.getId())));

        return result;
    }
}

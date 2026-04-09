/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package test;

import net.thevpc.scholar.hadrumaths.ComplexMatrix;
import net.thevpc.scholar.hadrumaths.Maths;

/**
 *
 * @author vpc
 */
public class TestMaths {
    public static void main(String[] args) {
        ComplexMatrix zerosMatrix = Maths.zerosMatrix(1);
        System.out.println(zerosMatrix);
    }
}

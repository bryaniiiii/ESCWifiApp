package com.example.mywifiapp2;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class MatrixTest {
    private Matrix testmatrix;
    private Matrix testmatrix2;
    private Matrix testmatrixAns;
    private Matrix temp;

    @Test
    public void MatrixAddTest() {
        testmatrix = new Matrix(10, 10);
        testmatrixAns = testmatrix;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                testmatrixAns.data[i][j] += 1;
            }
        }

        testmatrix2 = new Matrix(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                testmatrix2.data[i][j] = 1;
            }
        }
        assertEquals(testmatrixAns, testmatrix.add(testmatrix2));
    }

    @Test
    public void MatrixSubTest() {
        testmatrix = new Matrix(10, 10);
        testmatrixAns = testmatrix;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                testmatrixAns.data[i][j] -= 1;
            }
        }

        testmatrix2 = new Matrix(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                testmatrix2.data[i][j] = 1;
            }
        }
        assertEquals(testmatrixAns, testmatrix.subtract(testmatrix2));
    }

    @Test
    public void MatrixMulTest() {
        testmatrix = new Matrix(10, 10);
        testmatrixAns = testmatrix;
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                testmatrixAns.data[i][j] *= 2;
            }
        }

        testmatrix2 = new Matrix(10, 10);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                testmatrix2.data[i][j] = 2;
            }
        }
        assertEquals(testmatrixAns, testmatrix.multiply(testmatrix2));
    }
}
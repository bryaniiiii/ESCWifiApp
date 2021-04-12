package com.example.mywifiapp2;

import com.example.mywifiapp2.UI.SignUpActivity;
import com.example.mywifiapp2.UItest.ChooseImageTest;
import com.example.mywifiapp2.UItest.MainActivityTest;
import com.example.mywifiapp2.UItest.SignUpActivityTest;
import com.example.mywifiapp2.UItest.StartingActivityTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SignUpActivityTest.class,
        MainActivityTest.class,
        StartingActivityTest.class,
        ChooseImageTest.class
})

public class AndroidTestSuite {
}

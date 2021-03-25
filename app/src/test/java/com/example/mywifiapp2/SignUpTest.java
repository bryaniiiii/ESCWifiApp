package com.example.mywifiapp2;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;


import com.example.mywifiapp2.SignUpActivity;

@RunWith(PowerMockRunner.class)
@PrepareForTest(TextUtils.class)
public class SignUpTest {
    private SignUpActivity SignUp;

    private static final String fake_email = "gmail.com";
    private static final String fake_email2 = "@gmail";
    private static final String real_email = "abcde@gmail.com";

    @Before
    public void setUp() {
        PowerMockito.mockStatic(TextUtils.class);
        PowerMockito.when(TextUtils.isEmpty(any(CharSequence.class))).thenAnswer(new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                return !(a != null && a.length() > 0);
            }
        });
        SignUp = new SignUpActivity();
    }

    @Test
    public void validEmailTest() { assertTrue(SignUp.isValidEmail(real_email)); }

    @Test
    public void validEmailTest2() { assertFalse(SignUp.isValidEmail(fake_email)); }

    @Test
    public void validEmailTest3() { assertFalse(SignUp.isValidEmail(fake_email2)); }
}
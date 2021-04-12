package com.example.mywifiapp2.UItest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.mywifiapp2.R;
import com.example.mywifiapp2.UI.SignUpActivity;
import com.example.mywifiapp2.UI.StartingActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)

public class SignUpActivityTest {
    private ActivityScenario<SignUpActivity> testActivity;

    @Before
    public void setUp() throws Exception {
        testActivity = ActivityScenario.launch(SignUpActivity.class);
    }

    @Test
    public void ActivityInViewTest() {
        onView(ViewMatchers.withId(R.id.signUp)).check(matches(isDisplayed()));
    }

    @Test
    public void ComponentsVisibilityTest() {
        onView(withId(R.id.email)).check(matches(isDisplayed()));
        onView(withId(R.id.password1)).check(matches(isDisplayed()));
        onView(withId(R.id.password2)).check(matches(isDisplayed()));
        onView(withId(R.id.register)).check(matches(isDisplayed()));
        onView(withId(R.id.signinTv)).check(matches(isDisplayed()));
    }

    @Test
    public void SignInFunctionalityTest() {
        onView(withId(R.id.signinTv)).perform(click());
        onView(withId(R.id.main)).check(matches(isDisplayed()));
    }

//    @Test
//    public void SignUpFunctionalityTest() {
//        onView(withId(R.id.email)).perform(typeText("test@email.com"));
//        closeSoftKeyboard();
//        onView(withId(R.id.password)).perform(typeText("test1234"));
//        closeSoftKeyboard();
//        onView(withId(R.id.password)).perform(typeText("test1234"));
//        closeSoftKeyboard();
//        onView(withId(R.id.signUp)).perform(click());
//        onView(withId(R.id.starting)).check(matches(isDisplayed()));
//    }
}

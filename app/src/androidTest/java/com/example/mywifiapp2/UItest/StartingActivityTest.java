package com.example.mywifiapp2.UItest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.mywifiapp2.R;
import com.example.mywifiapp2.UI.StartingActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4ClassRunner.class)

public class StartingActivityTest {
    private ActivityScenario<StartingActivity> testActivity;

    @Before
    public void setUp() throws Exception {
        testActivity = ActivityScenario.launch(StartingActivity.class);
    }

    @Test
    public void ActivityInViewTest() {
        onView(ViewMatchers.withId(R.id.starting)).check(matches(isDisplayed()));
    }

    @Test
    public void ComponentsVisibilityTest() {
        onView(withId(R.id.mapme)).check(matches(isDisplayed()));
        onView(withId(R.id.locateme)).check(matches(isDisplayed()));
        onView(withId(R.id.logout)).check(matches(isDisplayed()));
    }

    @Test
    public void MappingFunctionalityTest() {
        onView(withId(R.id.mapme)).perform(click());
        onView(withId(R.id.chooseImage)).check(matches(isDisplayed()));
    }

    @Test
    public void TestingFunctionalityTest() {
        onView(withId(R.id.locateme)).perform(click());
        onView(withId(R.id.locate)).check(matches(isDisplayed()));
    }

    @Test
    public void LogOutFunctionalityTest() {
        onView(withId(R.id.logout)).perform(click());
        onView(withId(R.id.main)).check(matches(isDisplayed()));
    }

}
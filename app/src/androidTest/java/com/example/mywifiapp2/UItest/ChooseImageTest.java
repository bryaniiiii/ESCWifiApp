package com.example.mywifiapp2.UItest;


import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.mywifiapp2.R;
import com.example.mywifiapp2.UI.ChooseImage;
import com.example.mywifiapp2.UI.MainActivity;
import com.example.mywifiapp2.UI.StartingActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static android.app.Activity.RESULT_OK;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBackUnconditionally;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

    
public class ChooseImageTest {
//    private ActivityScenario<ChooseImage> testActivity;
//
//    @Mock
//    FirebaseUser user;

    @Before
    public void setUp() throws NullPointerException {
//        testActivity = ActivityScenario.launch(ChooseImage.class);
//        MockitoAnnotations.initMocks(this);
//        RxAndroidPlugins.reset();
//        RxAndroidPlugins.setInitMainThreadSchedulerHandler(schedulerCallable -> Schedulers.trampoline());
    }

    @Rule
    public IntentsTestRule<ChooseImage> intentsTestRule = new IntentsTestRule<>(ChooseImage.class);

    @Test
    public void validateFileChooserIntent(){
        Matcher<Intent> expectedIntent = hasAction(Intent.ACTION_GET_CONTENT);
        onView(withId(R.id.DeviceUpload)).check(matches(isDisplayed()));
        onView(withId(R.id.FirebaseUpload)).check(matches(isDisplayed()));
        onView(withId(R.id.DeviceUpload)).perform(click());
        intended(expectedIntent);

//        Instrumentation.ActivityResult activityResult = createGalleryPickActivityResultStub();
//        intending(expectedIntent).respondWith(activityResult);
//    }
//
//    private Instrumentation.ActivityResult createGalleryPickActivityResultStub() {
//        Resources resources = InstrumentationRegistry.getInstrumentation().getContext().getResources();
//        Uri imageUri = Uri.parse(
//                ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                resources.getResourcePackageName(R.drawable.ic_launcher_background) + "/" +
//                resources.getResourceTypeName(R.drawable.ic_launcher_background) + "/" +
//                resources.getResourceEntryName(R.drawable.ic_launcher_background)
//        );
//        Intent resultIntent = new Intent();
//        resultIntent.setData(imageUri);
//        return new Instrumentation.ActivityResult(RESULT_OK, resultIntent);
    }
}

package com.cegeptr.projetagectr.MainActivityTest;

import androidx.test.core.app.ActivityScenario;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.cegeptr.projetagectr.R;
import com.cegeptr.projetagectr.ui.MainActivity;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4ClassRunner.class)
public class NavigationTest
{
    @Test
    public void testMainActivityNavigation() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);

    }
}

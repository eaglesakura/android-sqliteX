package org.sqlite.database;

import com.eaglesakura.android.AndroidSupportTestCase;

import org.junit.runner.RunWith;

import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, packageName = BuildConfig.APPLICATION_ID, sdk = 21)
public abstract class UnitTestCase extends AndroidSupportTestCase {

}

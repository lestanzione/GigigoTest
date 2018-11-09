package br.com.stanzione.gigigotest.orders;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.Gravity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import br.com.stanzione.gigigotest.App;
import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.RecyclerViewItemCountAssertion;
import br.com.stanzione.gigigotest.data.Order;
import br.com.stanzione.gigigotest.di.ApplicationComponent;
import br.com.stanzione.gigigotest.di.DaggerApplicationComponent;
import br.com.stanzione.gigigotest.di.MockNetworkModule;
import br.com.stanzione.gigigotest.main.MainActivity;
import io.realm.Realm;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.DrawerMatchers.isClosed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class OrdersFragmentTest {

    @Rule
    public ActivityTestRule<MainActivity> activityRule = new ActivityTestRule<>(MainActivity.class, true, false);

    private MockWebServer server = new MockWebServer();
    private Realm realm;

    @Before
    public void setUp() throws Exception {
        setUpServer();
        setUpRealm();
        server.start();
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }

    public void setUpServer() {
        ApplicationComponent applicationComponent = DaggerApplicationComponent.builder()
                .networkModule(new MockNetworkModule(server))
                .build();

        ((App) getTargetContext().getApplicationContext())
                .setApplicationComponent(applicationComponent);
    }

    public void setUpRealm(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> realm.deleteAll());
        });
    }

    @Test
    public void withEmptyDatabaseShouldShowEmptyState() throws IOException, InterruptedException {

        server.enqueue(new MockResponse()
                .setBody(readFile("products_response.json")));

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.navOrders));

        onView(withId(R.id.orderRecyclerView))
                .check(matches(isDisplayed()));

        Thread.sleep(2000);

        onView(withId(R.id.orderRecyclerView))
                .check(new RecyclerViewItemCountAssertion(0));

        onView(withId(R.id.orderEmptyStateTextView))
                .check(matches(isDisplayed()));

    }

    @Test
    public void withDatabaseShouldShowOrderList() throws InterruptedException, IOException {

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                Order order = new Order();
                order.setId("any-fake-id");
                order.setTotalPrice(180.70);
                order.setTimestamp(new Date());
                order.setCardName("Leandro Stanzione");
                order.setEndOfCardNumber("1234");
                realm.copyToRealm(order);
            });
        });

        server.enqueue(new MockResponse()
                .setBody(readFile("products_response.json")));

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.navOrders));

        onView(withId(R.id.orderRecyclerView))
                .check(matches(isDisplayed()));

        Thread.sleep(2000);

        onView(withId(R.id.orderRecyclerView))
                .check(new RecyclerViewItemCountAssertion(1));

    }

    private String readFile(String fileName) throws IOException {
        InputStream stream = InstrumentationRegistry.getContext()
                .getAssets()
                .open(fileName);

        Source source = Okio.source(stream);
        BufferedSource buffer = Okio.buffer(source);

        return buffer.readUtf8();
    }

}
package br.com.stanzione.gigigotest.cardinfo;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;

import br.com.stanzione.gigigotest.App;
import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.RecyclerViewItemCountAssertion;
import br.com.stanzione.gigigotest.di.ApplicationComponent;
import br.com.stanzione.gigigotest.di.DaggerApplicationComponent;
import br.com.stanzione.gigigotest.di.MockNetworkModule;
import io.realm.Realm;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CardInfoActivityTest {

    @Rule
    public ActivityTestRule<CardInfoActivity> activityRule = new ActivityTestRule<>(CardInfoActivity.class, true, false);

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
    public void withWrongCardNumberShouldShowMessage(){

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.cardInfoNumberEditText))
                .perform(typeText("1234567890"));

        pressBack();

        onView(withId(R.id.finishPurchaseButton))
                .perform(click());

        onView(withId(R.id.cardInfoNumberEditText))
                .check(matches(hasErrorText(activityRule.getActivity().getString(R.string.error_card_number_length))));

    }

    @Test
    public void withWrongCardNameShouldShowMessage(){

        activityRule.launchActivity(new Intent());

        typeValidInNumber();

        onView(withId(R.id.cardInfoNameEditText))
                .perform(typeText(""));

        pressBack();

        onView(withId(R.id.finishPurchaseButton))
                .perform(click());

        onView(withId(R.id.cardInfoNameEditText))
                .check(matches(hasErrorText(activityRule.getActivity().getString(R.string.error_card_name_empty))));

    }

    @Test
    public void withWrongValidDateShouldShowMessage(){

        activityRule.launchActivity(new Intent());

        typeValidInNumber();
        typeValidInName();

        onView(withId(R.id.cardInfoValidEditText))
                .perform(typeText("2222"));

        pressBack();

        onView(withId(R.id.finishPurchaseButton))
                .perform(click());

        onView(withId(R.id.cardInfoValidEditText))
                .check(matches(hasErrorText(activityRule.getActivity().getString(R.string.error_card_valid))));

    }

    @Test
    public void withWrongCvvShouldShowMessage(){

        activityRule.launchActivity(new Intent());

        typeValidInNumber();
        typeValidInName();
        typeValidInDate();

        onView(withId(R.id.cardInfoCvvEditText))
                .perform(typeText("87"));

        pressBack();

        onView(withId(R.id.finishPurchaseButton))
                .perform(click());

        onView(withId(R.id.cardInfoCvvEditText))
                .check(matches(hasErrorText(activityRule.getActivity().getString(R.string.error_card_cvv_length))));

    }

    @Test
    public void withNetworkShouldProcessPurchase() throws IOException, InterruptedException {

        server.enqueue(new MockResponse()
                .setBody(readFile("create_order_response.json")));

        activityRule.launchActivity(new Intent());

        typeValidInNumber();
        typeValidInName();
        typeValidInDate();
        typeValidInCvv();

        pressBack();

        onView(withId(R.id.finishPurchaseButton))
                .perform(click());

        Thread.sleep(2000);

        onView(withText(R.string.title_dialog_purchased))
                .inRoot(isDialog())
                .check(matches(isDisplayed()));

    }

    @Test
    public void withGeneralErrorShouldShowGeneralErrorMessage() throws InterruptedException {

        server.enqueue(new MockResponse()
                .setResponseCode(500));

        activityRule.launchActivity(new Intent());

        typeValidInNumber();
        typeValidInName();
        typeValidInDate();
        typeValidInCvv();

        pressBack();

        onView(withId(R.id.finishPurchaseButton))
                .perform(click());

        Thread.sleep(2000);

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.message_general_error)))
                .check(matches(isDisplayed()));

    }

    @Test
    public void withNoConnectionShouldShowNoConnectionMessage() throws IOException, InterruptedException {

        server.shutdown();

        activityRule.launchActivity(new Intent());

        typeValidInNumber();
        typeValidInName();
        typeValidInDate();
        typeValidInCvv();

        pressBack();

        onView(withId(R.id.finishPurchaseButton))
                .perform(click());

        Thread.sleep(2000);

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.message_network_error)))
                .check(matches(isDisplayed()));

    }

    private void typeValidInNumber(){
        onView(withId(R.id.cardInfoNumberEditText))
                .perform(typeText("1234567890123456"));
    }

    private void typeValidInName(){
        onView(withId(R.id.cardInfoNameEditText))
                .perform(typeText("Leandro Stanzione"));
    }

    private void typeValidInDate(){
        onView(withId(R.id.cardInfoValidEditText))
                .perform(typeText("1119"));
    }

    private void typeValidInCvv(){
        onView(withId(R.id.cardInfoCvvEditText))
                .perform(typeText("371"));
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
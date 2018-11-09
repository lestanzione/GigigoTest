package br.com.stanzione.gigigotest.cart;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.GeneralLocation;
import android.support.test.espresso.action.GeneralSwipeAction;
import android.support.test.espresso.action.Press;
import android.support.test.espresso.action.Swipe;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.RecyclerViewItemCountAssertion;
import br.com.stanzione.gigigotest.data.CartItem;
import br.com.stanzione.gigigotest.util.CurrencyUtil;
import io.realm.Realm;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CartActivityTest {

    @Rule
    public ActivityTestRule<CartActivity> activityRule = new ActivityTestRule<>(CartActivity.class, true, false);

    private Realm realm;

    @Before
    public void setUp() {
        setUpRealm();
    }

    public void setUpRealm(){
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> realm.deleteAll());
        });
    }

    @Test
    public void withEmptyDatabaseShouldShowEmptyState() {

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.cartRecyclerView))
                .check(new RecyclerViewItemCountAssertion(0));

        onView(withId(R.id.cartEmptyStateTextView))
                .check(matches(isDisplayed()));

    }

    @Test
    public void withDatabaseShouldShowCartItemList() throws InterruptedException {

        double price1 = 14.70;
        double price2 = 7.35;
        int quantity1 = 3;
        int quantity2 = 2;

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                CartItem cartItem = new CartItem();
                cartItem.setId("any-fake-id");
                cartItem.setName("Fake Item");
                cartItem.setPrice(price1);
                cartItem.setQuantity(quantity1);
                realm.copyToRealm(cartItem);

                cartItem = new CartItem();
                cartItem.setId("another-fake-id");
                cartItem.setName("Another Fake Item");
                cartItem.setPrice(price2);
                cartItem.setQuantity(quantity2);
                realm.copyToRealm(cartItem);
            });
        });

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.cartRecyclerView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.cartRecyclerView))
                .check(new RecyclerViewItemCountAssertion(2));

        double finalPrice = (price1 * quantity1) + (price2 * quantity2);

        onView(withId(R.id.cartTotalAmountTextView))
                .check(matches(withText(CurrencyUtil.convertToBrazilianCurrency(finalPrice))));

        Thread.sleep(2000);

    }

    @Test
    public void withSwipeShouldShowDeleteCartItem() throws InterruptedException {

        double price1 = 14.70;
        double price2 = 7.35;
        int quantity1 = 3;
        int quantity2 = 2;

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                CartItem cartItem = new CartItem();
                cartItem.setId("any-fake-id");
                cartItem.setName("Fake Item");
                cartItem.setPrice(price1);
                cartItem.setQuantity(quantity1);
                realm.copyToRealm(cartItem);

                cartItem = new CartItem();
                cartItem.setId("another-fake-id");
                cartItem.setName("Another Fake Item");
                cartItem.setPrice(price2);
                cartItem.setQuantity(quantity2);
                realm.copyToRealm(cartItem);
            });
        });

        activityRule.launchActivity(new Intent());

        onView(withId(R.id.cartRecyclerView))
                .check(matches(isDisplayed()));

        onView(withId(R.id.cartRecyclerView))
                .check(new RecyclerViewItemCountAssertion(2));

        double finalPrice = (price1 * quantity1) + (price2 * quantity2);

        onView(withId(R.id.cartTotalAmountTextView))
                .check(matches(withText(CurrencyUtil.convertToBrazilianCurrency(finalPrice))));

        Thread.sleep(2000);

        onView(withId(R.id.cartRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, swipeToDelete()));

        Thread.sleep(2000);

        onView(withId(R.id.cartRecyclerView))
                .check(new RecyclerViewItemCountAssertion(1));

        finalPrice = (price2 * quantity2);

        onView(withId(R.id.cartTotalAmountTextView))
                .check(matches(withText(CurrencyUtil.convertToBrazilianCurrency(finalPrice))));

    }

    private static ViewAction swipeToDelete() {
        return new GeneralSwipeAction(Swipe.FAST, GeneralLocation.CENTER_RIGHT,
                GeneralLocation.CENTER_LEFT, Press.FINGER);
    }

}
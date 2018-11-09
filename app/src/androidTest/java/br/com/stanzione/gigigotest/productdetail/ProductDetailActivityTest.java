package br.com.stanzione.gigigotest.productdetail;

import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.stanzione.gigigotest.R;
import br.com.stanzione.gigigotest.data.Product;
import br.com.stanzione.gigigotest.util.Configs;
import br.com.stanzione.gigigotest.util.CurrencyUtil;
import io.realm.Realm;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class ProductDetailActivityTest {

    @Rule
    public ActivityTestRule<ProductDetailActivity> activityRule = new ActivityTestRule<>(ProductDetailActivity.class, true, false);

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
    public void withNoDatabaseShouldShowMessage() throws InterruptedException {

        activityRule.launchActivity(new Intent());

        onView(allOf(withId(android.support.design.R.id.snackbar_text), withText(R.string.message_general_error)))
                .check(matches(isDisplayed()));

        Thread.sleep(3000);

    }

    @Test
    public void withProductShouldShowProductDetail() throws InterruptedException {

        Product product = new Product();
        product.setId("any-fake-id");
        product.setName("Fake Product");
        product.setPrice(150.60);
        product.setImageUrl("https://media.shoes4you.com.br/79b4129bc818/camisa-polo-wg-clothing-masculina-cinza-claro.214x311.jpg");

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        instrumentation.runOnMainSync(() -> {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                realm.copyToRealm(product);
            });
        });

        activityRule.launchActivity(new Intent().putExtra(Configs.ARG_SELECTED_PRODUCT, "any-fake-id"));

        Thread.sleep(3000);

        onView(withId(R.id.productDetailNameTextView))
                .check(matches(withText(product.getName())));

        onView(withId(R.id.productDetailPriceTextView))
                .check(matches(withText(CurrencyUtil.convertToBrazilianCurrency(product.getPrice()))));

    }

}
package com.mosy.kalin.mosy.Services;

import android.content.Context;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.mosy.kalin.mosy.Async.Tasks.SearchVenuesAsyncTask;
import com.mosy.kalin.mosy.DTOs.Venue;
import com.mosy.kalin.mosy.Models.BindingModels.SearchVenuesBindingModel;

import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class VenuesServiceTests {

    @Mock
    private Context mMockContext;

    @Test
    public void blob_contentDownloaded() throws Exception {
        int maxResultsCount = 50;
        String query = "";
        double latitude = 1;
        double longitude = 1;
        SearchVenuesBindingModel model = new SearchVenuesBindingModel(maxResultsCount, latitude, longitude, query);
        ArrayList<Venue> venues = new SearchVenuesAsyncTask().execute(model).get();

        assertNotNull("Didn't manage to get Venues!", venues);
        assertTrue("Returned empty Venues array!", venues.size() > 0);
    }

}
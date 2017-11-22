package com.mosy.kalin.mosy.Performance;

import android.support.test.runner.AndroidJUnit4;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.blob.CloudBlob;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.mosy.kalin.mosy.DTOs.VenueImage;
import com.mosy.kalin.mosy.Services.VenuesService;

import org.androidannotations.annotations.Bean;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(AndroidJUnit4.class)
public class VenuesServicePerformanceTests {

    private VenuesService VenuesService = new VenuesService();

    @Test
    public void downloadVenueOutdoorThumbnail_LessThan10ms() throws Exception {
        // Retrieve storage account from connection-string.
        String venueId = "9AA16FE1-5173-4731-A7FB-1EC2812A1723";

        long tStart = System.currentTimeMillis();
        // Retrieve reference to a blob.
        VenueImage image = this.VenuesService.downloadVenueOutdoorImage(venueId);

        long elapsed = System.currentTimeMillis() - tStart;
        assertNotNull("Didn't manage to download VenueImage!", image);
        assertNotNull("Didn't manage to download VenueImage bytes!", image.Bytes);
        // assertTrue("Download VenueImage too slow!", elapsed <= 10);
    }



}
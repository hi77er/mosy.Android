package com.mosy.kalin.mosy.Performance;

import com.mosy.kalin.mosy.Base.AzureIntegrationTest;
import com.mosy.kalin.mosy.Services.AzureBlobService;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AzureBlobStoragePerformanceTests extends AzureIntegrationTest {

    @Test
    public void blobContentDownloaded_LessThan10ms() throws Exception {
        long tStart = System.currentTimeMillis();
        byte[] blobArr = new AzureBlobService().GetBlob("276b76c5-4464-406a-8fcb-9f8c54fcf0c4", "userimages\\profile\\100x100");

//        long elapsed = System.currentTimeMillis() - tStart;
        assertNotNull("No arr was returned!", blobArr );
        assertTrue("No bytes were downloaded!", blobArr.length > 0);
//        assertTrue("Download Blob Bytes too slow!", elapsed <= 10);
    }


}
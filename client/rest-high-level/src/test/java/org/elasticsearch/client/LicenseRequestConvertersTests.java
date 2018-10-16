/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.client;

import org.apache.http.client.methods.HttpPost;
import org.elasticsearch.action.support.master.AcknowledgedRequest;
import org.elasticsearch.client.license.StartBasicRequest;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.elasticsearch.client.license.DeleteLicenseRequest;
import org.elasticsearch.client.license.GetLicenseRequest;
import org.elasticsearch.client.license.PutLicenseRequest;
import org.elasticsearch.test.ESTestCase;

import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.client.RequestConvertersTests.setRandomMasterTimeout;
import static org.elasticsearch.client.RequestConvertersTests.setRandomTimeout;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;

public class LicenseRequestConvertersTests extends ESTestCase {
    public void testGetLicense() {
        final boolean local = randomBoolean();
        final GetLicenseRequest getLicenseRequest = new GetLicenseRequest();
        getLicenseRequest.setLocal(local);
        final Map<String, String> expectedParams = new HashMap<>();
        if (local) {
            expectedParams.put("local", Boolean.TRUE.toString());
        }

        Request request = LicenseRequestConverters.getLicense(getLicenseRequest);
        assertThat(request.getMethod(), equalTo(HttpGet.METHOD_NAME));
        assertThat(request.getEndpoint(), equalTo("/_xpack/license"));
        assertThat(request.getParameters(), equalTo(expectedParams));
        assertThat(request.getEntity(), is(nullValue()));
    }

    public void testPutLicense() {
        final boolean acknowledge = randomBoolean();
        final PutLicenseRequest putLicenseRequest = new PutLicenseRequest();
        putLicenseRequest.setAcknowledge(acknowledge);
        final Map<String, String> expectedParams = new HashMap<>();
        if (acknowledge) {
            expectedParams.put("acknowledge", Boolean.TRUE.toString());
        }
        setRandomTimeout(putLicenseRequest, AcknowledgedRequest.DEFAULT_ACK_TIMEOUT, expectedParams);
        setRandomMasterTimeout(putLicenseRequest, expectedParams);

        Request request = LicenseRequestConverters.putLicense(putLicenseRequest);
        assertThat(request.getMethod(), equalTo(HttpPut.METHOD_NAME));
        assertThat(request.getEndpoint(), equalTo("/_xpack/license"));
        assertThat(request.getParameters(), equalTo(expectedParams));
        assertThat(request.getEntity(), is(nullValue()));
    }

    public void testDeleteLicense() {
        final DeleteLicenseRequest deleteLicenseRequest = new DeleteLicenseRequest();
        final Map<String, String> expectedParams = new HashMap<>();
        setRandomTimeout(deleteLicenseRequest, AcknowledgedRequest.DEFAULT_ACK_TIMEOUT, expectedParams);
        setRandomMasterTimeout(deleteLicenseRequest, expectedParams);

        Request request = LicenseRequestConverters.deleteLicense(deleteLicenseRequest);
        assertThat(request.getMethod(), equalTo(HttpDelete.METHOD_NAME));
        assertThat(request.getEndpoint(), equalTo("/_xpack/license"));
        assertThat(request.getParameters(), equalTo(expectedParams));
        assertThat(request.getEntity(), is(nullValue()));
    }

    public void testStartBasic() {
        final boolean acknowledge = randomBoolean();
        StartBasicRequest startBasicRequest = new StartBasicRequest(acknowledge);
        Map<String, String> expectedParams = new HashMap<>();
        if (acknowledge) {
            expectedParams.put("acknowledge", Boolean.TRUE.toString());
        }

        setRandomTimeout(startBasicRequest, AcknowledgedRequest.DEFAULT_ACK_TIMEOUT, expectedParams);
        setRandomMasterTimeout(startBasicRequest, expectedParams);
        Request request = LicenseRequestConverters.startBasic(startBasicRequest);

        assertThat(request.getMethod(), equalTo(HttpPost.METHOD_NAME));
        assertThat(request.getEndpoint(), equalTo("/_xpack/license/start_basic"));
        assertThat(request.getParameters(), equalTo(expectedParams));
        assertThat(request.getEntity(), is(nullValue()));
    }
}

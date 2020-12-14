/**
 * Copyright 2012-2017 Thales Services SAS.
 *
 * This file is part of AuthzForce CE.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ow2.authzforce.core.pdp.testutil.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.JAXBException;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyIssuer;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Request;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Response;

import org.junit.Test;
import org.ow2.authzforce.core.pdp.api.io.PdpEngineInoutAdapter;
import org.ow2.authzforce.core.pdp.api.policy.PolicyVersion;
import org.ow2.authzforce.core.pdp.api.policy.PrimaryPolicyMetadata;
import org.ow2.authzforce.core.pdp.api.policy.TopLevelPolicyElementType;
import org.ow2.authzforce.core.pdp.impl.BasePdpEngine;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;
import org.ow2.authzforce.core.pdp.impl.io.PdpEngineAdapters;
import org.ow2.authzforce.core.pdp.testutil.PdpTest;
import org.ow2.authzforce.core.pdp.testutil.TestUtils;

/**
 * Test of {@link BasePdpEngine#getApplicablePolicies()}
 *
 */
public class PdpGetStaticApplicablePoliciesTest
{
	/**
	 * Name of directory that contains test resources for each test
	 */
	public final static String TEST_RESOURCES_DIRECTORY_LOCATION = "classpath:conformance/others/PolicyReference.Valid";

	private static final class MinimalPolicySetMetadata implements PrimaryPolicyMetadata
	{

		private final String id;
		private final PolicyVersion version;

		private MinimalPolicySetMetadata(final String id, final String version)
		{
			assert id != null && version != null;
			this.id = id;
			this.version = new PolicyVersion(version);
		}

		@Override
		public TopLevelPolicyElementType getType()
		{
			return TopLevelPolicyElementType.POLICY_SET;
		}

		@Override
		public String getId()
		{
			return this.id;
		}

		@Override
		public PolicyVersion getVersion()
		{
			return this.version;
		}

		@Override
		public Optional<PolicyIssuer> getIssuer()
		{
			return Optional.empty();
		}

		@Override
		public Optional<String> getDescription()
		{
			return Optional.empty();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString()
		{
			return "PolicySet['" + id + "' v" + version + "]";
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((version == null) ? 0 : version.hashCode());
			return result;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj)
		{
			if (this == obj)
			{
				return true;
			}

			if (!(obj instanceof PrimaryPolicyMetadata))
			{
				return false;
			}

			final PrimaryPolicyMetadata other = (PrimaryPolicyMetadata) obj;
			// Description metadata is ignored
			if (this.getIssuer().isPresent())
			{
				if (!(other.getIssuer().isPresent()))
				{
					return false;
				}

				if (!this.getIssuer().get().equals(other.getIssuer().get()))
				{
					return false;
				}
			}
			else if (other.getIssuer().isPresent())
			{
				return false;
			}

			return other.getType().equals(TopLevelPolicyElementType.POLICY_SET) && this.id.equals(other.getId()) && this.version.equals(other.getVersion());
		}

	}

	private final static PrimaryPolicyMetadata ROOT_POLICYSET_METADATA = new MinimalPolicySetMetadata("root:policyset-with-refs", "1.0");

	private final static List<PrimaryPolicyMetadata> REF_POLICYSET_METADATA_SET = Collections.singletonList(new MinimalPolicySetMetadata("PPS:Employee", "1.0"));

	@Test
	public void test() throws IllegalArgumentException, IOException, URISyntaxException, JAXBException
	{
		final String testResourceLocationPrefix = TEST_RESOURCES_DIRECTORY_LOCATION + "/";
		// Create PDP
		final PdpEngineConfiguration pdpEngineConf = TestUtils.newPdpEngineConfiguration(testResourceLocationPrefix + PdpTest.POLICY_FILENAME, testResourceLocationPrefix
				+ PdpTest.REF_POLICIES_DIR_NAME, false, null, null, null);
		try (final PdpEngineInoutAdapter<Request, Response> pdp = PdpEngineAdapters.newXacmlJaxbInoutAdapter(pdpEngineConf))
		{
			final Iterator<PrimaryPolicyMetadata> staticApplicablePolicies = pdp.getApplicablePolicies().iterator();
			assertTrue("No root policy in PDP's applicable policies (statically resolved)", staticApplicablePolicies != null && staticApplicablePolicies.hasNext());
			assertEquals("Invalid root policy in PDP's applicable policies (statically resolved)", ROOT_POLICYSET_METADATA, staticApplicablePolicies.next());

			for (final PrimaryPolicyMetadata expectedRefPolicyMeta : REF_POLICYSET_METADATA_SET)
			{
				assertTrue("No (more) referenced policy in PDP's applicable policies (statically resolved) although expected", staticApplicablePolicies.hasNext());
				assertEquals("Invalid referenced policy in PDP's applicable policies (statically resolved)", expectedRefPolicyMeta, staticApplicablePolicies.next());
			}

		}
	}
}

/*
 * Copyright 2016 Johannes Donath <johannesd@torchmind.com>
 * and other copyright owners as documented in the project's IP log.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.basinmc.faucet.plugin;

import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author <a href="mailto:johannesd@torchmind.com">Johannes Donath</a>
 */
public class PluginVersionTest {

    /**
     * Provides an assertion helper for {@link #testParse()}.
     *
     * @param version  The version to assert on.
     * @param major    The expected major bit.
     * @param minor    The expected minor bit.
     * @param patch    The expected patch bit.
     * @param extra    The expected extra bit (if any).
     * @param metadata The expected metadata bit (if any).
     */
    private void assertVersion(@Nonnull PluginVersion version, @Nonnegative int major, @Nonnegative int minor, @Nonnegative int patch, @Nullable String extra, @Nullable String metadata) {
        Assert.assertEquals(major, version.major());
        Assert.assertEquals(minor, version.minor());
        Assert.assertEquals(patch, version.patch());

        if (extra == null) {
            Assert.assertNull(version.extra());
        } else {
            Assert.assertEquals(extra, version.extra());
        }

        if (metadata == null) {
            Assert.assertNull(version.metadata());
        } else {
            Assert.assertEquals(metadata, version.metadata());
        }
    }

    /**
     * Tests {@link org.basinmc.faucet.plugin.PluginVersion#newerThan(PluginVersion)} and {@link
     * org.basinmc.faucet.plugin.PluginVersion#olderThan(PluginVersion)}.
     */
    @Test
    public void testCompare() {
        PluginVersion version00 = PluginVersion.builder().major(0).build(); // 0.0.0
        PluginVersion version01 = PluginVersion.builder().major(1).extra("snapshot").build(); // 1.0.0-snapshot
        PluginVersion version02 = PluginVersion.builder().major(1).extra("snapshot.1").build(); // 1.0.0-snapshot.1
        PluginVersion version03 = PluginVersion.builder().major(1).extra("snapshot.2").build(); // 1.0.0-snapshot.2
        PluginVersion version04 = PluginVersion.builder().major(1).extra("snapshot.3").build(); // 1.0.0-snapshot.3
        PluginVersion version05 = PluginVersion.builder().major(1).extra("alpha").build(); // 1.0.0-alpha
        PluginVersion version06 = PluginVersion.builder().major(1).extra("alpha.1").build(); // 1.0.0-alpha.1
        PluginVersion version07 = PluginVersion.builder().major(1).extra("alpha.2").build(); // 1.0.0-alpha.2
        PluginVersion version08 = PluginVersion.builder().major(1).extra("alpha.3").build(); // 1.0.0-alpha.3
        PluginVersion version09 = PluginVersion.builder().major(1).extra("beta").build(); // 1.0.0-beta
        PluginVersion version10 = PluginVersion.builder().major(1).extra("beta.1").build(); // 1.0.0-beta.1
        PluginVersion version11 = PluginVersion.builder().major(1).extra("beta.2").build(); // 1.0.0-beta.2
        PluginVersion version12 = PluginVersion.builder().major(1).extra("beta.3").build(); // 1.0.0-beta.3
        PluginVersion version13 = PluginVersion.builder().major(1).extra("rc").build(); // 1.0.0-rc
        PluginVersion version14 = PluginVersion.builder().major(1).extra("rc.1").build(); // 1.0.0-rc.1
        PluginVersion version15 = PluginVersion.builder().major(1).extra("rc.2").build(); // 1.0.0-rc.2
        PluginVersion version16 = PluginVersion.builder().major(1).extra("rc.3").build(); // 1.0.0-rc.3
        PluginVersion version17 = PluginVersion.builder().major(1).build(); // 1.0.0

        PluginVersion version18 = PluginVersion.builder().patch(0).build(); // 0.0.0
        PluginVersion version19 = PluginVersion.builder().patch(1).build(); // 0.0.1
        PluginVersion version20 = PluginVersion.builder().patch(2).build(); // 0.0.2
        PluginVersion version21 = PluginVersion.builder().patch(3).build(); // 0.0.3

        PluginVersion version22 = PluginVersion.builder().minor(0).build(); // 0.0.0
        PluginVersion version23 = PluginVersion.builder().minor(1).build(); // 0.1.0
        PluginVersion version24 = PluginVersion.builder().minor(2).build(); // 0.2.0
        PluginVersion version25 = PluginVersion.builder().minor(3).build(); // 0.3.0

        PluginVersion version26 = PluginVersion.builder().major(0).build(); // 0.0.0
        PluginVersion version27 = PluginVersion.builder().major(1).build(); // 1.0.0
        PluginVersion version28 = PluginVersion.builder().major(2).build(); // 2.0.0
        PluginVersion version29 = PluginVersion.builder().major(3).build(); // 3.0.0

        PluginVersion version30 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").build(); // 0.0.0-alpha
        PluginVersion version31 = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).build(); // 0.0.0
        PluginVersion version32 = PluginVersion.builder().major(0).minor(0).patch(1).extra(null).build(); // 0.0.1
        PluginVersion version33 = PluginVersion.builder().major(0).minor(1).patch(0).extra(null).build(); // 0.1.0
        PluginVersion version34 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).build(); // 1.0.0

        {
            Assert.assertTrue(version00.olderThan(version01));
            Assert.assertFalse(version00.newerThan(version01));
            Assert.assertEquals(-1, version00.compareTo(version01));
            Assert.assertTrue(version01.newerThan(version00));
            Assert.assertFalse(version01.olderThan(version00));
            Assert.assertEquals(1, version01.compareTo(version00));

            Assert.assertTrue(version01.olderThan(version02));
            Assert.assertFalse(version01.newerThan(version02));
            Assert.assertEquals(-1, version01.compareTo(version02));
            Assert.assertTrue(version02.newerThan(version01));
            Assert.assertFalse(version02.olderThan(version01));
            Assert.assertEquals(1, version02.compareTo(version01));

            Assert.assertTrue(version02.olderThan(version03));
            Assert.assertFalse(version02.newerThan(version03));
            Assert.assertEquals(-1, version02.compareTo(version03));
            Assert.assertTrue(version03.newerThan(version02));
            Assert.assertFalse(version03.olderThan(version02));
            Assert.assertEquals(1, version03.compareTo(version02));

            Assert.assertTrue(version03.olderThan(version04));
            Assert.assertFalse(version03.newerThan(version04));
            Assert.assertEquals(-1, version03.compareTo(version04));
            Assert.assertTrue(version04.newerThan(version03));
            Assert.assertFalse(version04.olderThan(version03));
            Assert.assertEquals(1, version04.compareTo(version03));

            Assert.assertTrue(version04.olderThan(version05));
            Assert.assertFalse(version04.newerThan(version05));
            Assert.assertEquals(-1, version04.compareTo(version05));
            Assert.assertTrue(version05.newerThan(version04));
            Assert.assertFalse(version05.olderThan(version04));
            Assert.assertEquals(1, version05.compareTo(version04));

            Assert.assertTrue(version05.olderThan(version06));
            Assert.assertFalse(version05.newerThan(version06));
            Assert.assertEquals(-1, version05.compareTo(version06));
            Assert.assertTrue(version06.newerThan(version05));
            Assert.assertFalse(version06.olderThan(version05));
            Assert.assertEquals(1, version06.compareTo(version05));

            Assert.assertTrue(version06.olderThan(version07));
            Assert.assertFalse(version06.newerThan(version07));
            Assert.assertEquals(-1, version06.compareTo(version07));
            Assert.assertTrue(version07.newerThan(version06));
            Assert.assertFalse(version07.olderThan(version06));
            Assert.assertEquals(1, version07.compareTo(version06));

            Assert.assertTrue(version07.olderThan(version08));
            Assert.assertFalse(version07.newerThan(version08));
            Assert.assertEquals(-1, version07.compareTo(version08));
            Assert.assertTrue(version08.newerThan(version07));
            Assert.assertFalse(version08.olderThan(version07));
            Assert.assertEquals(1, version08.compareTo(version07));

            Assert.assertTrue(version08.olderThan(version09));
            Assert.assertFalse(version08.newerThan(version09));
            Assert.assertEquals(-1, version08.compareTo(version09));
            Assert.assertTrue(version09.newerThan(version08));
            Assert.assertFalse(version09.olderThan(version08));
            Assert.assertEquals(1, version09.compareTo(version08));

            Assert.assertTrue(version09.olderThan(version10));
            Assert.assertFalse(version09.newerThan(version10));
            Assert.assertEquals(-1, version09.compareTo(version10));
            Assert.assertTrue(version10.newerThan(version09));
            Assert.assertFalse(version10.olderThan(version09));
            Assert.assertEquals(1, version10.compareTo(version09));

            Assert.assertTrue(version10.olderThan(version11));
            Assert.assertFalse(version10.newerThan(version11));
            Assert.assertEquals(-1, version10.compareTo(version11));
            Assert.assertTrue(version11.newerThan(version10));
            Assert.assertFalse(version11.olderThan(version10));
            Assert.assertEquals(1, version11.compareTo(version10));

            Assert.assertTrue(version11.olderThan(version12));
            Assert.assertFalse(version11.newerThan(version12));
            Assert.assertEquals(-1, version11.compareTo(version12));
            Assert.assertTrue(version12.newerThan(version11));
            Assert.assertFalse(version12.olderThan(version11));
            Assert.assertEquals(1, version12.compareTo(version11));

            Assert.assertTrue(version12.olderThan(version13));
            Assert.assertFalse(version12.newerThan(version13));
            Assert.assertEquals(-1, version12.compareTo(version13));
            Assert.assertTrue(version13.newerThan(version12));
            Assert.assertFalse(version13.olderThan(version12));
            Assert.assertEquals(1, version13.compareTo(version12));

            Assert.assertTrue(version13.olderThan(version14));
            Assert.assertFalse(version13.newerThan(version14));
            Assert.assertEquals(-1, version13.compareTo(version14));
            Assert.assertTrue(version14.newerThan(version13));
            Assert.assertFalse(version14.olderThan(version13));
            Assert.assertEquals(1, version14.compareTo(version13));

            Assert.assertTrue(version14.olderThan(version15));
            Assert.assertFalse(version14.newerThan(version15));
            Assert.assertEquals(-1, version14.compareTo(version15));
            Assert.assertTrue(version15.newerThan(version14));
            Assert.assertFalse(version15.olderThan(version14));
            Assert.assertEquals(1, version15.compareTo(version14));

            Assert.assertTrue(version15.olderThan(version16));
            Assert.assertFalse(version15.newerThan(version16));
            Assert.assertEquals(-1, version15.compareTo(version16));
            Assert.assertTrue(version16.newerThan(version15));
            Assert.assertFalse(version16.olderThan(version15));
            Assert.assertEquals(1, version16.compareTo(version15));

            Assert.assertTrue(version16.olderThan(version17));
            Assert.assertFalse(version16.newerThan(version17));
            Assert.assertEquals(-1, version16.compareTo(version17));
            Assert.assertTrue(version17.newerThan(version16));
            Assert.assertFalse(version17.olderThan(version16));
            Assert.assertEquals(1, version17.compareTo(version16));
        }

        {
            Assert.assertTrue(version18.olderThan(version19));
            Assert.assertFalse(version18.newerThan(version19));
            Assert.assertEquals(-1, version18.compareTo(version19));
            Assert.assertTrue(version19.newerThan(version18));
            Assert.assertFalse(version19.olderThan(version18));
            Assert.assertEquals(1, version19.compareTo(version18));

            Assert.assertTrue(version19.olderThan(version20));
            Assert.assertFalse(version19.newerThan(version20));
            Assert.assertEquals(-1, version19.compareTo(version20));
            Assert.assertTrue(version20.newerThan(version19));
            Assert.assertFalse(version20.olderThan(version19));
            Assert.assertEquals(1, version20.compareTo(version19));

            Assert.assertTrue(version20.olderThan(version21));
            Assert.assertFalse(version20.newerThan(version21));
            Assert.assertEquals(-1, version20.compareTo(version21));
            Assert.assertTrue(version21.newerThan(version20));
            Assert.assertFalse(version21.olderThan(version20));
            Assert.assertEquals(1, version21.compareTo(version20));
        }

        {
            Assert.assertTrue(version22.olderThan(version23));
            Assert.assertFalse(version22.newerThan(version23));
            Assert.assertEquals(-1, version22.compareTo(version23));
            Assert.assertTrue(version23.newerThan(version22));
            Assert.assertFalse(version23.olderThan(version22));
            Assert.assertEquals(1, version23.compareTo(version22));

            Assert.assertTrue(version23.olderThan(version24));
            Assert.assertFalse(version23.newerThan(version24));
            Assert.assertEquals(-1, version23.compareTo(version24));
            Assert.assertTrue(version24.newerThan(version23));
            Assert.assertFalse(version24.olderThan(version23));
            Assert.assertEquals(1, version24.compareTo(version23));

            Assert.assertTrue(version24.olderThan(version25));
            Assert.assertFalse(version24.newerThan(version25));
            Assert.assertEquals(-1, version24.compareTo(version25));
            Assert.assertTrue(version25.newerThan(version24));
            Assert.assertFalse(version25.olderThan(version24));
            Assert.assertEquals(1, version25.compareTo(version24));
        }

        {
            Assert.assertTrue(version26.olderThan(version27));
            Assert.assertFalse(version26.newerThan(version27));
            Assert.assertEquals(-1, version26.compareTo(version27));
            Assert.assertTrue(version27.newerThan(version26));
            Assert.assertFalse(version27.olderThan(version26));
            Assert.assertEquals(1, version27.compareTo(version26));

            Assert.assertTrue(version27.olderThan(version28));
            Assert.assertFalse(version27.newerThan(version28));
            Assert.assertEquals(-1, version27.compareTo(version28));
            Assert.assertTrue(version28.newerThan(version27));
            Assert.assertFalse(version28.olderThan(version27));
            Assert.assertEquals(1, version28.compareTo(version27));

            Assert.assertTrue(version28.olderThan(version29));
            Assert.assertFalse(version28.newerThan(version29));
            Assert.assertEquals(-1, version28.compareTo(version29));
            Assert.assertTrue(version29.newerThan(version28));
            Assert.assertFalse(version29.olderThan(version28));
            Assert.assertEquals(1, version29.compareTo(version28));
        }

        {
            Assert.assertTrue(version30.olderThan(version31));
            Assert.assertFalse(version30.newerThan(version31));
            Assert.assertEquals(-1, version30.compareTo(version31));
            Assert.assertTrue(version31.newerThan(version30));
            Assert.assertFalse(version31.olderThan(version30));
            Assert.assertEquals(1, version31.compareTo(version30));

            Assert.assertTrue(version31.olderThan(version32));
            Assert.assertFalse(version31.newerThan(version32));
            Assert.assertEquals(-1, version31.compareTo(version32));
            Assert.assertTrue(version32.newerThan(version31));
            Assert.assertFalse(version32.olderThan(version31));
            Assert.assertEquals(1, version32.compareTo(version31));

            Assert.assertTrue(version32.olderThan(version33));
            Assert.assertFalse(version32.newerThan(version33));
            Assert.assertEquals(-1, version32.compareTo(version33));
            Assert.assertTrue(version33.newerThan(version32));
            Assert.assertFalse(version33.olderThan(version32));
            Assert.assertEquals(1, version33.compareTo(version32));

            Assert.assertTrue(version33.olderThan(version34));
            Assert.assertFalse(version33.newerThan(version34));
            Assert.assertEquals(-1, version33.compareTo(version34));
            Assert.assertTrue(version34.newerThan(version33));
            Assert.assertFalse(version34.olderThan(version33));
            Assert.assertEquals(1, version34.compareTo(version33));
        }
    }

    /**
     * Tests {@link org.basinmc.faucet.plugin.PluginVersion#equals(PluginVersion)}.
     */
    @Test
    public void testEquals() {
        PluginVersion version00 = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata(null).build();
        PluginVersion version01 = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata(null).build();
        PluginVersion version02 = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata("metadata").build();

        PluginVersion version03 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot").metadata(null).build();
        PluginVersion version04 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot").metadata(null).build();
        PluginVersion version05 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot").metadata("metadata").build();

        PluginVersion version06 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.1").metadata(null).build();
        PluginVersion version07 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.1").metadata(null).build();
        PluginVersion version08 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.1").metadata("metadata").build();

        PluginVersion version09 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.2").metadata(null).build();
        PluginVersion version10 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.2").metadata(null).build();
        PluginVersion version11 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.2").metadata("metadata").build();

        PluginVersion version12 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata(null).build();
        PluginVersion version13 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata(null).build();
        PluginVersion version14 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a").metadata(null).build();
        PluginVersion version15 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata("metadata").build();
        PluginVersion version16 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a").metadata("metadata").build();

        PluginVersion version17 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.1").metadata(null).build();
        PluginVersion version18 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.1").metadata(null).build();
        PluginVersion version19 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a.1").metadata(null).build();
        PluginVersion version20 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.1").metadata("metadata").build();
        PluginVersion version21 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a.1").metadata("metadata").build();

        PluginVersion version22 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.2").metadata(null).build();
        PluginVersion version23 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.2").metadata(null).build();
        PluginVersion version24 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a.2").metadata(null).build();
        PluginVersion version25 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.2").metadata("metadata").build();
        PluginVersion version26 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a.2").metadata("metadata").build();

        PluginVersion version27 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta").metadata(null).build();
        PluginVersion version28 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta").metadata(null).build();
        PluginVersion version29 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b").metadata(null).build();
        PluginVersion version30 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta").metadata("metadata").build();
        PluginVersion version31 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b").metadata("metadata").build();

        PluginVersion version32 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.1").metadata(null).build();
        PluginVersion version33 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.1").metadata(null).build();
        PluginVersion version34 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b.1").metadata(null).build();
        PluginVersion version35 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.1").metadata("metadata").build();
        PluginVersion version36 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b.1").metadata("metadata").build();

        PluginVersion version37 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.2").metadata(null).build();
        PluginVersion version38 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.2").metadata(null).build();
        PluginVersion version39 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b.2").metadata(null).build();
        PluginVersion version40 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.2").metadata("metadata").build();
        PluginVersion version41 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b.2").metadata("metadata").build();

        PluginVersion version42 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc").metadata(null).build();
        PluginVersion version43 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc").metadata(null).build();
        PluginVersion version44 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc").metadata("metadata").build();

        PluginVersion version45 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.1").metadata(null).build();
        PluginVersion version46 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.1").metadata(null).build();
        PluginVersion version47 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.1").metadata("metadata").build();

        PluginVersion version48 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.2").metadata(null).build();
        PluginVersion version49 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.2").metadata(null).build();
        PluginVersion version50 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.2").metadata("metadata").build();

        PluginVersion version51 = PluginVersion.builder().major(0).minor(0).patch(1).extra(null).metadata(null).build();
        PluginVersion version52 = PluginVersion.builder().major(0).minor(0).patch(1).extra(null).metadata(null).build();
        PluginVersion version53 = PluginVersion.builder().major(0).minor(0).patch(1).extra(null).metadata("metadata").build();

        PluginVersion version54 = PluginVersion.builder().major(0).minor(1).patch(0).extra(null).metadata(null).build();
        PluginVersion version55 = PluginVersion.builder().major(0).minor(1).patch(0).extra(null).metadata(null).build();
        PluginVersion version56 = PluginVersion.builder().major(0).minor(1).patch(0).extra(null).metadata("metadata").build();

        PluginVersion version57 = PluginVersion.builder().major(0).minor(1).patch(1).extra(null).metadata(null).build();
        PluginVersion version58 = PluginVersion.builder().major(0).minor(1).patch(1).extra(null).metadata(null).build();
        PluginVersion version59 = PluginVersion.builder().major(0).minor(1).patch(1).extra(null).metadata("metadata").build();

        PluginVersion version60 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata(null).build();
        PluginVersion version61 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata(null).build();
        PluginVersion version62 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata("metadata").build();

        PluginVersion version63 = PluginVersion.builder().major(1).minor(0).patch(1).extra(null).metadata(null).build();
        PluginVersion version64 = PluginVersion.builder().major(1).minor(0).patch(1).extra(null).metadata(null).build();
        PluginVersion version65 = PluginVersion.builder().major(1).minor(0).patch(1).extra(null).metadata("metadata").build();

        PluginVersion version66 = PluginVersion.builder().major(1).minor(1).patch(0).extra(null).metadata(null).build();
        PluginVersion version67 = PluginVersion.builder().major(1).minor(1).patch(0).extra(null).metadata(null).build();
        PluginVersion version68 = PluginVersion.builder().major(1).minor(1).patch(0).extra(null).metadata("metadata").build();

        PluginVersion version69 = PluginVersion.builder().major(1).minor(1).patch(1).extra(null).metadata(null).build();
        PluginVersion version70 = PluginVersion.builder().major(1).minor(1).patch(1).extra(null).metadata(null).build();
        PluginVersion version71 = PluginVersion.builder().major(1).minor(1).patch(1).extra(null).metadata("metadata").build();

        {
            PluginVersion current = version00;

            Assert.assertEquals(current, version01);
            Assert.assertEquals(current, version02);

            // Assert.assertNotEquals (current, version00);
            // Assert.assertNotEquals (current, version01);
            // Assert.assertNotEquals (current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version03;

            Assert.assertEquals(current, version04);
            Assert.assertEquals(current, version05);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            // Assert.assertNotEquals (current, version03);
            // Assert.assertNotEquals (current, version04);
            // Assert.assertNotEquals (current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version06;

            Assert.assertEquals(current, version07);
            Assert.assertEquals(current, version08);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            // Assert.assertNotEquals (current, version06);
            // Assert.assertNotEquals (current, version07);
            // Assert.assertNotEquals (current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version09;

            Assert.assertEquals(current, version10);
            Assert.assertEquals(current, version11);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            // Assert.assertNotEquals (current, version09);
            // Assert.assertNotEquals (current, version10);
            // Assert.assertNotEquals (current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version12;

            Assert.assertEquals(current, version13);
            Assert.assertEquals(current, version14);
            Assert.assertEquals(current, version15);
            Assert.assertEquals(current, version16);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            // Assert.assertNotEquals (current, version12);
            // Assert.assertNotEquals (current, version13);
            // Assert.assertNotEquals (current, version14);
            // Assert.assertNotEquals (current, version15);
            // Assert.assertNotEquals (current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version17;

            Assert.assertEquals(current, version18);
            Assert.assertEquals(current, version19);
            Assert.assertEquals(current, version20);
            Assert.assertEquals(current, version21);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            // Assert.assertNotEquals (current, version17);
            // Assert.assertNotEquals (current, version18);
            // Assert.assertNotEquals (current, version19);
            // Assert.assertNotEquals (current, version20);
            // Assert.assertNotEquals (current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version22;

            Assert.assertEquals(current, version23);
            Assert.assertEquals(current, version24);
            Assert.assertEquals(current, version25);
            Assert.assertEquals(current, version26);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            // Assert.assertNotEquals (current, version22);
            // Assert.assertNotEquals (current, version23);
            // Assert.assertNotEquals (current, version24);
            // Assert.assertNotEquals (current, version25);
            // Assert.assertNotEquals (current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version27;

            Assert.assertEquals(current, version28);
            Assert.assertEquals(current, version29);
            Assert.assertEquals(current, version30);
            Assert.assertEquals(current, version31);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            // Assert.assertNotEquals (current, version27);
            // Assert.assertNotEquals (current, version28);
            // Assert.assertNotEquals (current, version29);
            // Assert.assertNotEquals (current, version30);
            // Assert.assertNotEquals (current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version32;

            Assert.assertEquals(current, version33);
            Assert.assertEquals(current, version34);
            Assert.assertEquals(current, version35);
            Assert.assertEquals(current, version36);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            // Assert.assertNotEquals (current, version32);
            // Assert.assertNotEquals (current, version33);
            // Assert.assertNotEquals (current, version34);
            // Assert.assertNotEquals (current, version35);
            // Assert.assertNotEquals (current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version37;

            Assert.assertEquals(current, version38);
            Assert.assertEquals(current, version39);
            Assert.assertEquals(current, version40);
            Assert.assertEquals(current, version41);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            // Assert.assertNotEquals (current, version37);
            // Assert.assertNotEquals (current, version38);
            // Assert.assertNotEquals (current, version39);
            // Assert.assertNotEquals (current, version40);
            // Assert.assertNotEquals (current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version42;

            Assert.assertEquals(current, version43);
            Assert.assertEquals(current, version44);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            // Assert.assertNotEquals (current, version42);
            // Assert.assertNotEquals (current, version43);
            // Assert.assertNotEquals (current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version45;

            Assert.assertEquals(current, version46);
            Assert.assertEquals(current, version47);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            // Assert.assertNotEquals (current, version45);
            // Assert.assertNotEquals (current, version46);
            // Assert.assertNotEquals (current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version48;

            Assert.assertEquals(current, version49);
            Assert.assertEquals(current, version50);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            // Assert.assertNotEquals (current, version48);
            // Assert.assertNotEquals (current, version49);
            // Assert.assertNotEquals (current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version51;

            Assert.assertEquals(current, version52);
            Assert.assertEquals(current, version53);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            // Assert.assertNotEquals (current, version51);
            // Assert.assertNotEquals (current, version52);
            // Assert.assertNotEquals (current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version54;

            Assert.assertEquals(current, version55);
            Assert.assertEquals(current, version56);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            // Assert.assertNotEquals (current, version54);
            // Assert.assertNotEquals (current, version55);
            // Assert.assertNotEquals (current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version57;

            Assert.assertEquals(current, version58);
            Assert.assertEquals(current, version59);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            // Assert.assertNotEquals (current, version57);
            // Assert.assertNotEquals (current, version58);
            // Assert.assertNotEquals (current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version60;

            Assert.assertEquals(current, version61);
            Assert.assertEquals(current, version62);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            // Assert.assertNotEquals (current, version60);
            // Assert.assertNotEquals (current, version61);
            // Assert.assertNotEquals (current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version63;

            Assert.assertEquals(current, version64);
            Assert.assertEquals(current, version65);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            // Assert.assertNotEquals (current, version63);
            // Assert.assertNotEquals (current, version64);
            // Assert.assertNotEquals (current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version66;

            Assert.assertEquals(current, version67);
            Assert.assertEquals(current, version68);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            // Assert.assertNotEquals (current, version66);
            // Assert.assertNotEquals (current, version67);
            // Assert.assertNotEquals (current, version68);
            Assert.assertNotEquals(current, version69);
            Assert.assertNotEquals(current, version70);
            Assert.assertNotEquals(current, version71);
        }

        {
            PluginVersion current = version69;

            Assert.assertEquals(current, version70);
            Assert.assertEquals(current, version71);

            Assert.assertNotEquals(current, version00);
            Assert.assertNotEquals(current, version01);
            Assert.assertNotEquals(current, version02);
            Assert.assertNotEquals(current, version03);
            Assert.assertNotEquals(current, version04);
            Assert.assertNotEquals(current, version05);
            Assert.assertNotEquals(current, version06);
            Assert.assertNotEquals(current, version07);
            Assert.assertNotEquals(current, version08);
            Assert.assertNotEquals(current, version09);
            Assert.assertNotEquals(current, version10);
            Assert.assertNotEquals(current, version11);
            Assert.assertNotEquals(current, version12);
            Assert.assertNotEquals(current, version13);
            Assert.assertNotEquals(current, version14);
            Assert.assertNotEquals(current, version15);
            Assert.assertNotEquals(current, version16);
            Assert.assertNotEquals(current, version17);
            Assert.assertNotEquals(current, version18);
            Assert.assertNotEquals(current, version19);
            Assert.assertNotEquals(current, version20);
            Assert.assertNotEquals(current, version21);
            Assert.assertNotEquals(current, version22);
            Assert.assertNotEquals(current, version23);
            Assert.assertNotEquals(current, version24);
            Assert.assertNotEquals(current, version25);
            Assert.assertNotEquals(current, version26);
            Assert.assertNotEquals(current, version27);
            Assert.assertNotEquals(current, version28);
            Assert.assertNotEquals(current, version29);
            Assert.assertNotEquals(current, version30);
            Assert.assertNotEquals(current, version31);
            Assert.assertNotEquals(current, version32);
            Assert.assertNotEquals(current, version33);
            Assert.assertNotEquals(current, version34);
            Assert.assertNotEquals(current, version35);
            Assert.assertNotEquals(current, version36);
            Assert.assertNotEquals(current, version37);
            Assert.assertNotEquals(current, version38);
            Assert.assertNotEquals(current, version39);
            Assert.assertNotEquals(current, version40);
            Assert.assertNotEquals(current, version41);
            Assert.assertNotEquals(current, version42);
            Assert.assertNotEquals(current, version43);
            Assert.assertNotEquals(current, version44);
            Assert.assertNotEquals(current, version45);
            Assert.assertNotEquals(current, version46);
            Assert.assertNotEquals(current, version47);
            Assert.assertNotEquals(current, version48);
            Assert.assertNotEquals(current, version49);
            Assert.assertNotEquals(current, version50);
            Assert.assertNotEquals(current, version51);
            Assert.assertNotEquals(current, version52);
            Assert.assertNotEquals(current, version53);
            Assert.assertNotEquals(current, version54);
            Assert.assertNotEquals(current, version55);
            Assert.assertNotEquals(current, version56);
            Assert.assertNotEquals(current, version57);
            Assert.assertNotEquals(current, version58);
            Assert.assertNotEquals(current, version59);
            Assert.assertNotEquals(current, version60);
            Assert.assertNotEquals(current, version61);
            Assert.assertNotEquals(current, version62);
            Assert.assertNotEquals(current, version63);
            Assert.assertNotEquals(current, version64);
            Assert.assertNotEquals(current, version65);
            Assert.assertNotEquals(current, version66);
            Assert.assertNotEquals(current, version67);
            Assert.assertNotEquals(current, version68);
            // Assert.assertNotEquals (current, version69);
            // Assert.assertNotEquals (current, version70);
            // Assert.assertNotEquals (current, version71);
        }
    }

    /**
     * Tests {@link PluginVersion#hashCode()}.
     */
    @Test
    public void testHashCode() {
        PluginVersion version01 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot").metadata(null).build();
        PluginVersion version02 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot").metadata(null).build();
        PluginVersion version03 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot").metadata("metadata").build();

        PluginVersion version04 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.1").metadata(null).build();
        PluginVersion version05 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.1").metadata(null).build();
        PluginVersion version06 = PluginVersion.builder().major(0).minor(0).patch(0).extra("snapshot.1").metadata("metadata").build();

        PluginVersion version07 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata(null).build();
        PluginVersion version08 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata(null).build();
        PluginVersion version09 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata("metadata").build();
        PluginVersion version10 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a").metadata(null).build();
        PluginVersion version11 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a").metadata(null).build();
        PluginVersion version12 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a").metadata("metadata").build();

        PluginVersion version13 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.1").metadata(null).build();
        PluginVersion version14 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.1").metadata(null).build();
        PluginVersion version15 = PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha.1").metadata("metadata").build();
        PluginVersion version16 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a.1").metadata(null).build();
        PluginVersion version17 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a.1").metadata(null).build();
        PluginVersion version18 = PluginVersion.builder().major(0).minor(0).patch(0).extra("a.1").metadata("metadata").build();

        PluginVersion version19 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta").metadata(null).build();
        PluginVersion version20 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta").metadata(null).build();
        PluginVersion version21 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta").metadata("metadata").build();
        PluginVersion version22 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b").metadata(null).build();
        PluginVersion version23 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b").metadata(null).build();
        PluginVersion version24 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b").metadata("metadata").build();

        PluginVersion version25 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.1").metadata(null).build();
        PluginVersion version26 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.1").metadata(null).build();
        PluginVersion version27 = PluginVersion.builder().major(0).minor(0).patch(0).extra("beta.1").metadata("metadata").build();
        PluginVersion version28 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b.1").metadata(null).build();
        PluginVersion version29 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b.1").metadata(null).build();
        PluginVersion version30 = PluginVersion.builder().major(0).minor(0).patch(0).extra("b.1").metadata("metadata").build();

        PluginVersion version31 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc").metadata(null).build();
        PluginVersion version32 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc").metadata(null).build();
        PluginVersion version33 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc").metadata("metadata").build();

        PluginVersion version34 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.1").metadata(null).build();
        PluginVersion version35 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.1").metadata(null).build();
        PluginVersion version36 = PluginVersion.builder().major(0).minor(0).patch(0).extra("rc.1").metadata("metadata").build();

        PluginVersion version37 = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata(null).build();
        PluginVersion version38 = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata(null).build();
        PluginVersion version39 = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata("metadata").build();

        PluginVersion version40 = PluginVersion.builder().major(0).minor(0).patch(1).extra(null).metadata(null).build();
        PluginVersion version41 = PluginVersion.builder().major(0).minor(0).patch(1).extra(null).metadata(null).build();
        PluginVersion version42 = PluginVersion.builder().major(0).minor(0).patch(1).extra(null).metadata("metadata").build();

        PluginVersion version43 = PluginVersion.builder().major(0).minor(1).patch(1).extra(null).metadata(null).build();
        PluginVersion version44 = PluginVersion.builder().major(0).minor(1).patch(1).extra(null).metadata(null).build();
        PluginVersion version45 = PluginVersion.builder().major(0).minor(1).patch(1).extra(null).metadata("metadata").build();

        PluginVersion version46 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata(null).build();
        PluginVersion version47 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata(null).build();
        PluginVersion version48 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata("metadata").build();

        PluginVersion version49 = PluginVersion.builder().major(1).minor(0).patch(1).extra(null).metadata(null).build();
        PluginVersion version50 = PluginVersion.builder().major(1).minor(0).patch(1).extra(null).metadata(null).build();
        PluginVersion version51 = PluginVersion.builder().major(1).minor(0).patch(1).extra(null).metadata("metadata").build();

        PluginVersion version52 = PluginVersion.builder().major(1).minor(1).patch(0).extra(null).metadata(null).build();
        PluginVersion version53 = PluginVersion.builder().major(1).minor(1).patch(0).extra(null).metadata(null).build();
        PluginVersion version54 = PluginVersion.builder().major(1).minor(1).patch(0).extra(null).metadata("metadata").build();

        PluginVersion version55 = PluginVersion.builder().major(1).minor(1).patch(1).extra(null).metadata(null).build();
        PluginVersion version56 = PluginVersion.builder().major(1).minor(1).patch(1).extra(null).metadata(null).build();
        PluginVersion version57 = PluginVersion.builder().major(1).minor(1).patch(1).extra(null).metadata("metadata").build();

        {
            Assert.assertEquals(version01.hashCode(), version02.hashCode());
            Assert.assertEquals(version01.hashCode(), version03.hashCode());
        }

        {
            Assert.assertEquals(version04.hashCode(), version05.hashCode());
            Assert.assertEquals(version04.hashCode(), version06.hashCode());
        }

        {
            Assert.assertEquals(version07.hashCode(), version08.hashCode());
            Assert.assertEquals(version07.hashCode(), version09.hashCode());
            Assert.assertEquals(version07.hashCode(), version10.hashCode());
            Assert.assertEquals(version07.hashCode(), version11.hashCode());
            Assert.assertEquals(version07.hashCode(), version12.hashCode());
        }

        {
            Assert.assertEquals(version13.hashCode(), version14.hashCode());
            Assert.assertEquals(version13.hashCode(), version15.hashCode());
            Assert.assertEquals(version13.hashCode(), version16.hashCode());
            Assert.assertEquals(version13.hashCode(), version17.hashCode());
            Assert.assertEquals(version13.hashCode(), version18.hashCode());
        }

        {
            Assert.assertEquals(version19.hashCode(), version20.hashCode());
            Assert.assertEquals(version19.hashCode(), version21.hashCode());
            Assert.assertEquals(version19.hashCode(), version22.hashCode());
            Assert.assertEquals(version19.hashCode(), version23.hashCode());
            Assert.assertEquals(version19.hashCode(), version24.hashCode());
        }

        {
            Assert.assertEquals(version25.hashCode(), version26.hashCode());
            Assert.assertEquals(version25.hashCode(), version27.hashCode());
            Assert.assertEquals(version25.hashCode(), version28.hashCode());
            Assert.assertEquals(version25.hashCode(), version29.hashCode());
            Assert.assertEquals(version25.hashCode(), version30.hashCode());
        }

        {
            Assert.assertEquals(version31.hashCode(), version32.hashCode());
            Assert.assertEquals(version31.hashCode(), version33.hashCode());
        }

        {
            Assert.assertEquals(version34.hashCode(), version35.hashCode());
            Assert.assertEquals(version34.hashCode(), version36.hashCode());
        }

        {
            Assert.assertEquals(version37.hashCode(), version38.hashCode());
            Assert.assertEquals(version37.hashCode(), version39.hashCode());
        }

        {
            Assert.assertEquals(version40.hashCode(), version41.hashCode());
            Assert.assertEquals(version40.hashCode(), version42.hashCode());
        }

        {
            Assert.assertEquals(version43.hashCode(), version44.hashCode());
            Assert.assertEquals(version43.hashCode(), version45.hashCode());
        }

        {
            Assert.assertEquals(version46.hashCode(), version47.hashCode());
            Assert.assertEquals(version46.hashCode(), version48.hashCode());
        }

        {
            Assert.assertEquals(version49.hashCode(), version50.hashCode());
            Assert.assertEquals(version49.hashCode(), version51.hashCode());
        }

        {
            Assert.assertEquals(version52.hashCode(), version53.hashCode());
            Assert.assertEquals(version52.hashCode(), version54.hashCode());
        }

        {
            Assert.assertEquals(version55.hashCode(), version56.hashCode());
            Assert.assertEquals(version55.hashCode(), version57.hashCode());
        }
    }

    /**
     * Tests {@link PluginVersion#extra(String)}, {@link PluginVersion#major(int)}, {@link
     * PluginVersion#metadata(String)}, {@link PluginVersion#minor(int)} and {@link
     * PluginVersion#patch(int)}.
     */
    @Test
    public void testMutate() {
        PluginVersion version = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata(null).build();

        {
            PluginVersion mutatedVersion = version.major(1);

            Assert.assertEquals(0, version.major());
            Assert.assertEquals(0, version.minor());
            Assert.assertEquals(0, version.patch());
            Assert.assertNull(version.extra());
            Assert.assertNull(version.metadata());

            Assert.assertEquals(1, mutatedVersion.major());
            Assert.assertEquals(0, mutatedVersion.minor());
            Assert.assertEquals(0, mutatedVersion.patch());
            Assert.assertNull(mutatedVersion.extra());
            Assert.assertNull(mutatedVersion.metadata());
        }

        {
            PluginVersion mutatedVersion = version.minor(1);

            Assert.assertEquals(0, version.major());
            Assert.assertEquals(0, version.minor());
            Assert.assertEquals(0, version.patch());
            Assert.assertNull(version.extra());
            Assert.assertNull(version.metadata());

            Assert.assertEquals(0, mutatedVersion.major());
            Assert.assertEquals(1, mutatedVersion.minor());
            Assert.assertEquals(0, mutatedVersion.patch());
            Assert.assertNull(mutatedVersion.extra());
            Assert.assertNull(mutatedVersion.metadata());
        }

        {
            PluginVersion mutatedVersion = version.patch(1);

            Assert.assertEquals(0, version.major());
            Assert.assertEquals(0, version.minor());
            Assert.assertEquals(0, version.patch());
            Assert.assertNull(version.extra());
            Assert.assertNull(version.metadata());

            Assert.assertEquals(0, mutatedVersion.major());
            Assert.assertEquals(0, mutatedVersion.minor());
            Assert.assertEquals(1, mutatedVersion.patch());
            Assert.assertNull(mutatedVersion.extra());
            Assert.assertNull(mutatedVersion.metadata());
        }

        {
            PluginVersion mutatedVersion = version.extra("alpha");

            Assert.assertEquals(0, version.major());
            Assert.assertEquals(0, version.minor());
            Assert.assertEquals(0, version.patch());
            Assert.assertNull(version.extra());
            Assert.assertNull(version.metadata());

            Assert.assertEquals(0, mutatedVersion.major());
            Assert.assertEquals(0, mutatedVersion.minor());
            Assert.assertEquals(0, mutatedVersion.patch());
            Assert.assertEquals("alpha", mutatedVersion.extra());
            Assert.assertNull(mutatedVersion.metadata());
        }

        {
            PluginVersion mutatedVersion = version.metadata("metadata");

            Assert.assertEquals(0, version.major());
            Assert.assertEquals(0, version.minor());
            Assert.assertEquals(0, version.patch());
            Assert.assertNull(version.extra());
            Assert.assertNull(version.metadata());

            Assert.assertEquals(0, mutatedVersion.major());
            Assert.assertEquals(0, mutatedVersion.minor());
            Assert.assertEquals(0, mutatedVersion.patch());
            Assert.assertNull(mutatedVersion.extra());
            Assert.assertEquals("metadata", mutatedVersion.metadata());
        }
    }

    /**
     * Tests {@link org.basinmc.faucet.plugin.PluginVersion#of(String)}.
     */
    @Test
    public void testParse() {
        this.assertVersion(PluginVersion.of("0.0"), 0, 0, 0, null, null);
        this.assertVersion(PluginVersion.of("0.0+metadata"), 0, 0, 0, null, "metadata");
        this.assertVersion(PluginVersion.of("0.0-snapshot"), 0, 0, 0, "snapshot", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-snapshot"), 0, 0, 0, "snapshot", "metadata");
        this.assertVersion(PluginVersion.of("0.0-snapshot+metadata"), 0, 0, 0, "snapshot", "metadata");

        this.assertVersion(PluginVersion.of("0.0-snapshot.1"), 0, 0, 0, "snapshot.1", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-snapshot.1"), 0, 0, 0, "snapshot.1", "metadata");
        this.assertVersion(PluginVersion.of("0.0-snapshot.1+metadata"), 0, 0, 0, "snapshot.1", "metadata");

        this.assertVersion(PluginVersion.of("0.0-snapshot.2"), 0, 0, 0, "snapshot.2", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-snapshot.2"), 0, 0, 0, "snapshot.2", "metadata");
        this.assertVersion(PluginVersion.of("0.0-snapshot.2+metadata"), 0, 0, 0, "snapshot.2", "metadata");

        this.assertVersion(PluginVersion.of("0.0-alpha"), 0, 0, 0, "alpha", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-alpha"), 0, 0, 0, "alpha", "metadata");
        this.assertVersion(PluginVersion.of("0.0-alpha+metadata"), 0, 0, 0, "alpha", "metadata");

        this.assertVersion(PluginVersion.of("0.0-a"), 0, 0, 0, "a", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-a"), 0, 0, 0, "a", "metadata");
        this.assertVersion(PluginVersion.of("0.0-a+metadata"), 0, 0, 0, "a", "metadata");

        this.assertVersion(PluginVersion.of("0.0-alpha.1"), 0, 0, 0, "alpha.1", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-alpha.1"), 0, 0, 0, "alpha.1", "metadata");
        this.assertVersion(PluginVersion.of("0.0-alpha.1+metadata"), 0, 0, 0, "alpha.1", "metadata");

        this.assertVersion(PluginVersion.of("0.0-a.1"), 0, 0, 0, "a.1", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-a.1"), 0, 0, 0, "a.1", "metadata");
        this.assertVersion(PluginVersion.of("0.0-a.1+metadata"), 0, 0, 0, "a.1", "metadata");

        this.assertVersion(PluginVersion.of("0.0-alpha.2"), 0, 0, 0, "alpha.2", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-alpha.2"), 0, 0, 0, "alpha.2", "metadata");
        this.assertVersion(PluginVersion.of("0.0-alpha.2+metadata"), 0, 0, 0, "alpha.2", "metadata");

        this.assertVersion(PluginVersion.of("0.0-a.2"), 0, 0, 0, "a.2", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-a.2"), 0, 0, 0, "a.2", "metadata");
        this.assertVersion(PluginVersion.of("0.0-a.2+metadata"), 0, 0, 0, "a.2", "metadata");

        this.assertVersion(PluginVersion.of("0.0-beta"), 0, 0, 0, "beta", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-beta"), 0, 0, 0, "beta", "metadata");
        this.assertVersion(PluginVersion.of("0.0-beta+metadata"), 0, 0, 0, "beta", "metadata");

        this.assertVersion(PluginVersion.of("0.0-b"), 0, 0, 0, "b", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-b"), 0, 0, 0, "b", "metadata");
        this.assertVersion(PluginVersion.of("0.0-b+metadata"), 0, 0, 0, "b", "metadata");

        this.assertVersion(PluginVersion.of("0.0-beta.1"), 0, 0, 0, "beta.1", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-beta.1"), 0, 0, 0, "beta.1", "metadata");
        this.assertVersion(PluginVersion.of("0.0-beta.1+metadata"), 0, 0, 0, "beta.1", "metadata");

        this.assertVersion(PluginVersion.of("0.0-b.1"), 0, 0, 0, "b.1", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-b.1"), 0, 0, 0, "b.1", "metadata");
        this.assertVersion(PluginVersion.of("0.0-b.1+metadata"), 0, 0, 0, "b.1", "metadata");

        this.assertVersion(PluginVersion.of("0.0-beta.2"), 0, 0, 0, "beta.2", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-beta.2"), 0, 0, 0, "beta.2", "metadata");
        this.assertVersion(PluginVersion.of("0.0-beta.2+metadata"), 0, 0, 0, "beta.2", "metadata");

        this.assertVersion(PluginVersion.of("0.0-b.2"), 0, 0, 0, "b.2", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-b.2"), 0, 0, 0, "b.2", "metadata");
        this.assertVersion(PluginVersion.of("0.0-b.2+metadata"), 0, 0, 0, "b.2", "metadata");

        this.assertVersion(PluginVersion.of("0.0-rc"), 0, 0, 0, "rc", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-rc"), 0, 0, 0, "rc", "metadata");
        this.assertVersion(PluginVersion.of("0.0-rc+metadata"), 0, 0, 0, "rc", "metadata");

        this.assertVersion(PluginVersion.of("0.0-rc.1"), 0, 0, 0, "rc.1", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-rc.1"), 0, 0, 0, "rc.1", "metadata");
        this.assertVersion(PluginVersion.of("0.0-rc.1+metadata"), 0, 0, 0, "rc.1", "metadata");

        this.assertVersion(PluginVersion.of("0.0-rc.2"), 0, 0, 0, "rc.2", null);
        this.assertVersion(PluginVersion.of("0.0+metadata-rc.2"), 0, 0, 0, "rc.2", "metadata");
        this.assertVersion(PluginVersion.of("0.0-rc.2+metadata"), 0, 0, 0, "rc.2", "metadata");

        this.assertVersion(PluginVersion.of("0.1"), 0, 1, 0, null, null);
        this.assertVersion(PluginVersion.of("1.0"), 1, 0, 0, null, null);
        this.assertVersion(PluginVersion.of("1.1"), 1, 1, 0, null, null);

        this.assertVersion(PluginVersion.of("0.0.1"), 0, 0, 1, null, null);
        this.assertVersion(PluginVersion.of("0.1.0"), 0, 1, 0, null, null);
        this.assertVersion(PluginVersion.of("0.1.1"), 0, 1, 1, null, null);
        this.assertVersion(PluginVersion.of("1.0.0"), 1, 0, 0, null, null);
        this.assertVersion(PluginVersion.of("1.0.1"), 1, 0, 1, null, null);
        this.assertVersion(PluginVersion.of("1.1.0"), 1, 1, 0, null, null);
        this.assertVersion(PluginVersion.of("1.1.1"), 1, 1, 1, null, null);
    }

    /**
     * Tests {@link org.basinmc.faucet.plugin.PluginVersion#range(String)}.
     */
    @Test
    public void testParseRange() {
        VersionRange range0 = PluginVersion.range("(0.0,1.0)");
        VersionRange range1 = PluginVersion.range("[0.0,1.0)");
        VersionRange range2 = PluginVersion.range("(0.0,1.0]");
        VersionRange range3 = PluginVersion.range("[0.0,1.0]");

        {
            Assert.assertEquals(PluginVersion.builder().major(0).minor(0).build(), range0.startBound());
            Assert.assertEquals(PluginVersion.builder().major(1).minor(0).build(), range0.endBound());
            Assert.assertFalse(range0.startInclusive());
            Assert.assertFalse(range0.endInclusive());
        }

        {
            Assert.assertEquals(PluginVersion.builder().major(0).minor(0).build(), range1.startBound());
            Assert.assertEquals(PluginVersion.builder().major(1).minor(0).build(), range1.endBound());
            Assert.assertTrue(range1.startInclusive());
            Assert.assertFalse(range1.endInclusive());
        }

        {
            Assert.assertEquals(PluginVersion.builder().major(0).minor(0).build(), range2.startBound());
            Assert.assertEquals(PluginVersion.builder().major(1).minor(0).build(), range2.endBound());
            Assert.assertFalse(range2.startInclusive());
            Assert.assertTrue(range2.endInclusive());
        }

        {
            Assert.assertEquals(PluginVersion.builder().major(0).minor(0).build(), range3.startBound());
            Assert.assertEquals(PluginVersion.builder().major(1).minor(0).build(), range3.endBound());
            Assert.assertTrue(range3.startInclusive());
            Assert.assertTrue(range3.endInclusive());
        }
    }

    /**
     * Tests {@link PluginVersion#stable()} and {@link PluginVersion#unstable()}.
     */
    @Test
    public void testStability() {
        PluginVersion version00 = PluginVersion.builder().major(0).minor(0).patch(0).extra(null).build();
        PluginVersion version01 = PluginVersion.builder().major(0).minor(0).patch(1).extra(null).build();
        PluginVersion version02 = PluginVersion.builder().major(0).minor(1).patch(0).extra(null).build();
        PluginVersion version03 = PluginVersion.builder().major(0).minor(1).patch(1).extra(null).build();
        PluginVersion version04 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).build();
        PluginVersion version05 = PluginVersion.builder().major(1).minor(0).patch(1).extra(null).build();
        PluginVersion version06 = PluginVersion.builder().major(1).minor(1).patch(0).extra(null).build();
        PluginVersion version07 = PluginVersion.builder().major(1).minor(1).patch(1).extra(null).build();
        PluginVersion version08 = PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata("metadata").build();

        PluginVersion version09 = PluginVersion.builder().major(1).minor(0).patch(0).extra("snapshot").build();
        PluginVersion version10 = PluginVersion.builder().major(1).minor(0).patch(0).extra("snapshot.1").build();
        PluginVersion version11 = PluginVersion.builder().major(1).minor(0).patch(0).extra("snapshot.2").build();
        PluginVersion version12 = PluginVersion.builder().major(1).minor(0).patch(0).extra("alpha").build();
        PluginVersion version13 = PluginVersion.builder().major(1).minor(0).patch(0).extra("alpha.1").build();
        PluginVersion version14 = PluginVersion.builder().major(1).minor(0).patch(0).extra("alpha.2").build();
        PluginVersion version15 = PluginVersion.builder().major(1).minor(0).patch(0).extra("a").build();
        PluginVersion version16 = PluginVersion.builder().major(1).minor(0).patch(0).extra("a.1").build();
        PluginVersion version17 = PluginVersion.builder().major(1).minor(0).patch(0).extra("a.2").build();
        PluginVersion version18 = PluginVersion.builder().major(1).minor(0).patch(0).extra("beta").build();
        PluginVersion version19 = PluginVersion.builder().major(1).minor(0).patch(0).extra("beta.1").build();
        PluginVersion version20 = PluginVersion.builder().major(1).minor(0).patch(0).extra("beta.2").build();
        PluginVersion version21 = PluginVersion.builder().major(1).minor(0).patch(0).extra("b").build();
        PluginVersion version22 = PluginVersion.builder().major(1).minor(0).patch(0).extra("b.1").build();
        PluginVersion version23 = PluginVersion.builder().major(1).minor(0).patch(0).extra("b.2").build();
        PluginVersion version24 = PluginVersion.builder().major(1).minor(0).patch(0).extra("rc").build();
        PluginVersion version25 = PluginVersion.builder().major(1).minor(0).patch(0).extra("rc.1").build();
        PluginVersion version26 = PluginVersion.builder().major(1).minor(0).patch(0).extra("rc.2").build();
        PluginVersion version27 = PluginVersion.builder().major(1).minor(0).patch(0).extra("foo").build();
        PluginVersion version28 = PluginVersion.builder().major(1).minor(0).patch(0).extra("foo.1").build();
        PluginVersion version29 = PluginVersion.builder().major(1).minor(0).patch(0).extra("foo.2").build();

        {
            Assert.assertFalse(version00.stable());
            Assert.assertTrue(version00.unstable());

            Assert.assertFalse(version01.stable());
            Assert.assertTrue(version01.unstable());

            Assert.assertFalse(version02.stable());
            Assert.assertTrue(version02.unstable());

            Assert.assertFalse(version03.stable());
            Assert.assertTrue(version03.unstable());

            Assert.assertTrue(version04.stable());
            Assert.assertFalse(version04.unstable());

            Assert.assertTrue(version05.stable());
            Assert.assertFalse(version05.unstable());

            Assert.assertTrue(version06.stable());
            Assert.assertFalse(version06.unstable());

            Assert.assertTrue(version07.stable());
            Assert.assertFalse(version07.unstable());

            Assert.assertTrue(version08.stable());
            Assert.assertFalse(version08.unstable());
        }

        {
            Assert.assertFalse(version09.stable());
            Assert.assertTrue(version09.unstable());

            Assert.assertFalse(version10.stable());
            Assert.assertTrue(version10.unstable());

            Assert.assertFalse(version11.stable());
            Assert.assertTrue(version11.unstable());

            Assert.assertFalse(version12.stable());
            Assert.assertTrue(version12.unstable());

            Assert.assertFalse(version13.stable());
            Assert.assertTrue(version13.unstable());

            Assert.assertFalse(version14.stable());
            Assert.assertTrue(version14.unstable());

            Assert.assertFalse(version15.stable());
            Assert.assertTrue(version15.unstable());

            Assert.assertFalse(version16.stable());
            Assert.assertTrue(version16.unstable());

            Assert.assertFalse(version17.stable());
            Assert.assertTrue(version17.unstable());

            Assert.assertFalse(version18.stable());
            Assert.assertTrue(version18.unstable());

            Assert.assertFalse(version19.stable());
            Assert.assertTrue(version19.unstable());

            Assert.assertFalse(version20.stable());
            Assert.assertTrue(version20.unstable());

            Assert.assertFalse(version21.stable());
            Assert.assertTrue(version21.unstable());

            Assert.assertFalse(version22.stable());
            Assert.assertTrue(version22.unstable());

            Assert.assertFalse(version23.stable());
            Assert.assertTrue(version23.unstable());

            Assert.assertFalse(version24.stable());
            Assert.assertTrue(version24.unstable());

            Assert.assertFalse(version25.stable());
            Assert.assertTrue(version25.unstable());

            Assert.assertFalse(version26.stable());
            Assert.assertTrue(version26.unstable());

            Assert.assertFalse(version27.stable());
            Assert.assertTrue(version27.unstable());

            Assert.assertFalse(version28.stable());
            Assert.assertTrue(version28.unstable());

            Assert.assertFalse(version29.stable());
            Assert.assertTrue(version29.unstable());
        }
    }

    /**
     * Tests {@link PluginVersion#toString()}.
     */
    @Test
    public void testToString() {
        Assert.assertEquals("0.0-alpha", PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata(null).build().toString());
        Assert.assertEquals("0.0-alpha+metadata", PluginVersion.builder().major(0).minor(0).patch(0).extra("alpha").metadata("metadata").build().toString());
        Assert.assertEquals("0.0", PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata(null).build().toString());
        Assert.assertEquals("0.0+metadata", PluginVersion.builder().major(0).minor(0).patch(0).extra(null).metadata("metadata").build().toString());

        Assert.assertEquals("0.0.1-alpha", PluginVersion.builder().major(0).minor(0).patch(1).extra("alpha").metadata(null).build().toString());
        Assert.assertEquals("0.0.1-alpha+metadata", PluginVersion.builder().major(0).minor(0).patch(1).extra("alpha").metadata("metadata").build().toString());
        Assert.assertEquals("0.0.1", PluginVersion.builder().major(0).minor(0).patch(1).extra(null).metadata(null).build().toString());
        Assert.assertEquals("0.0.1+metadata", PluginVersion.builder().major(0).minor(0).patch(1).extra(null).metadata("metadata").build().toString());

        Assert.assertEquals("0.1-alpha", PluginVersion.builder().major(0).minor(1).patch(0).extra("alpha").metadata(null).build().toString());
        Assert.assertEquals("0.1-alpha+metadata", PluginVersion.builder().major(0).minor(1).patch(0).extra("alpha").metadata("metadata").build().toString());
        Assert.assertEquals("0.1", PluginVersion.builder().major(0).minor(1).patch(0).extra(null).metadata(null).build().toString());
        Assert.assertEquals("0.1+metadata", PluginVersion.builder().major(0).minor(1).patch(0).extra(null).metadata("metadata").build().toString());

        Assert.assertEquals("0.1.1-alpha", PluginVersion.builder().major(0).minor(1).patch(1).extra("alpha").metadata(null).build().toString());
        Assert.assertEquals("0.1.1-alpha+metadata", PluginVersion.builder().major(0).minor(1).patch(1).extra("alpha").metadata("metadata").build().toString());
        Assert.assertEquals("0.1.1", PluginVersion.builder().major(0).minor(1).patch(1).extra(null).metadata(null).build().toString());
        Assert.assertEquals("0.1.1+metadata", PluginVersion.builder().major(0).minor(1).patch(1).extra(null).metadata("metadata").build().toString());

        Assert.assertEquals("1.0-alpha", PluginVersion.builder().major(1).minor(0).patch(0).extra("alpha").metadata(null).build().toString());
        Assert.assertEquals("1.0-alpha+metadata", PluginVersion.builder().major(1).minor(0).patch(0).extra("alpha").metadata("metadata").build().toString());
        Assert.assertEquals("1.0", PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata(null).build().toString());
        Assert.assertEquals("1.0+metadata", PluginVersion.builder().major(1).minor(0).patch(0).extra(null).metadata("metadata").build().toString());

        Assert.assertEquals("1.0.1-alpha", PluginVersion.builder().major(1).minor(0).patch(1).extra("alpha").metadata(null).build().toString());
        Assert.assertEquals("1.0.1-alpha+metadata", PluginVersion.builder().major(1).minor(0).patch(1).extra("alpha").metadata("metadata").build().toString());
        Assert.assertEquals("1.0.1", PluginVersion.builder().major(1).minor(0).patch(1).extra(null).metadata(null).build().toString());
        Assert.assertEquals("1.0.1+metadata", PluginVersion.builder().major(1).minor(0).patch(1).extra(null).metadata("metadata").build().toString());

        Assert.assertEquals("1.1-alpha", PluginVersion.builder().major(1).minor(1).patch(0).extra("alpha").metadata(null).build().toString());
        Assert.assertEquals("1.1-alpha+metadata", PluginVersion.builder().major(1).minor(1).patch(0).extra("alpha").metadata("metadata").build().toString());
        Assert.assertEquals("1.1", PluginVersion.builder().major(1).minor(1).patch(0).extra(null).metadata(null).build().toString());
        Assert.assertEquals("1.1+metadata", PluginVersion.builder().major(1).minor(1).patch(0).extra(null).metadata("metadata").build().toString());

        Assert.assertEquals("1.1.1-alpha", PluginVersion.builder().major(1).minor(1).patch(1).extra("alpha").metadata(null).build().toString());
        Assert.assertEquals("1.1.1-alpha+metadata", PluginVersion.builder().major(1).minor(1).patch(1).extra("alpha").metadata("metadata").build().toString());
        Assert.assertEquals("1.1.1", PluginVersion.builder().major(1).minor(1).patch(1).extra(null).metadata(null).build().toString());
        Assert.assertEquals("1.1.1+metadata", PluginVersion.builder().major(1).minor(1).patch(1).extra(null).metadata("metadata").build().toString());
    }

    /**
     * Provides test cases for {@link org.basinmc.faucet.plugin.PluginVersion.Builder}.
     */
    public static class BuilderTest {

        /**
         * Tests {@link org.basinmc.faucet.plugin.PluginVersion.Builder#build()}.
         */
        @Test
        public void testBuild() {
            PluginVersion.Builder builder00 = PluginVersion.builder();
            PluginVersion.Builder builder01 = PluginVersion.builder().metadata("metadata");
            PluginVersion.Builder builder02 = PluginVersion.builder().extra("extra");
            PluginVersion.Builder builder03 = PluginVersion.builder().extra("extra").metadata("metadata");
            PluginVersion.Builder builder04 = PluginVersion.builder().patch(1);
            PluginVersion.Builder builder05 = PluginVersion.builder().patch(1).metadata("metadata");
            PluginVersion.Builder builder06 = PluginVersion.builder().patch(1).extra("extra");
            PluginVersion.Builder builder07 = PluginVersion.builder().patch(1).extra("extra").metadata("metadata");
            PluginVersion.Builder builder08 = PluginVersion.builder().minor(1);
            PluginVersion.Builder builder09 = PluginVersion.builder().minor(1).metadata("metadata");
            PluginVersion.Builder builder10 = PluginVersion.builder().minor(1).extra("extra");
            PluginVersion.Builder builder11 = PluginVersion.builder().minor(1).extra("extra").metadata("metadata");
            PluginVersion.Builder builder12 = PluginVersion.builder().minor(1).patch(1);
            PluginVersion.Builder builder13 = PluginVersion.builder().minor(1).patch(1).metadata("metadata");
            PluginVersion.Builder builder14 = PluginVersion.builder().minor(1).patch(1).extra("extra");
            PluginVersion.Builder builder15 = PluginVersion.builder().minor(1).patch(1).extra("extra").metadata("metadata");
            PluginVersion.Builder builder16 = PluginVersion.builder().major(1);
            PluginVersion.Builder builder17 = PluginVersion.builder().major(1).metadata("metadata");
            PluginVersion.Builder builder18 = PluginVersion.builder().major(1).extra("extra");
            PluginVersion.Builder builder19 = PluginVersion.builder().major(1).extra("extra").metadata("metadata");
            PluginVersion.Builder builder20 = PluginVersion.builder().major(1).patch(1);
            PluginVersion.Builder builder21 = PluginVersion.builder().major(1).patch(1).metadata("metadata");
            PluginVersion.Builder builder22 = PluginVersion.builder().major(1).patch(1).extra("extra");
            PluginVersion.Builder builder23 = PluginVersion.builder().major(1).patch(1).extra("extra").metadata("metadata");
            PluginVersion.Builder builder24 = PluginVersion.builder().major(1).minor(1);
            PluginVersion.Builder builder25 = PluginVersion.builder().major(1).minor(1).metadata("metadata");
            PluginVersion.Builder builder26 = PluginVersion.builder().major(1).minor(1).extra("extra");
            PluginVersion.Builder builder27 = PluginVersion.builder().major(1).minor(1).extra("extra").metadata("metadata");
            PluginVersion.Builder builder28 = PluginVersion.builder().major(1).minor(1).patch(1);
            PluginVersion.Builder builder29 = PluginVersion.builder().major(1).minor(1).patch(1).metadata("metadata");
            PluginVersion.Builder builder30 = PluginVersion.builder().major(1).minor(1).patch(1).extra("extra");
            PluginVersion.Builder builder31 = PluginVersion.builder().major(1).minor(1).patch(1).extra("extra").metadata("metadata");

            {
                Assert.assertEquals(0, builder00.major());
                Assert.assertEquals(0, builder00.minor());
                Assert.assertEquals(0, builder00.patch());
                Assert.assertNull(builder00.extra());
                Assert.assertNull(builder00.metadata());

                PluginVersion version = builder00.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(0, builder01.major());
                Assert.assertEquals(0, builder01.minor());
                Assert.assertEquals(0, builder01.patch());
                Assert.assertNull(builder01.extra());
                Assert.assertEquals("metadata", builder01.metadata());

                PluginVersion version = builder01.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(0, builder02.major());
                Assert.assertEquals(0, builder02.minor());
                Assert.assertEquals(0, builder02.patch());
                Assert.assertEquals("extra", builder02.extra());
                Assert.assertNull(builder02.metadata());

                PluginVersion version = builder02.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(0, builder03.major());
                Assert.assertEquals(0, builder03.minor());
                Assert.assertEquals(0, builder03.patch());
                Assert.assertEquals("extra", builder03.extra());
                Assert.assertEquals("metadata", builder03.metadata());

                PluginVersion version = builder03.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(0, builder04.major());
                Assert.assertEquals(0, builder04.minor());
                Assert.assertEquals(1, builder04.patch());
                Assert.assertNull(builder04.extra());
                Assert.assertNull(builder04.metadata());

                PluginVersion version = builder04.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(0, builder05.major());
                Assert.assertEquals(0, builder05.minor());
                Assert.assertEquals(1, builder05.patch());
                Assert.assertNull(builder05.extra());
                Assert.assertEquals("metadata", builder05.metadata());

                PluginVersion version = builder05.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(0, builder06.major());
                Assert.assertEquals(0, builder06.minor());
                Assert.assertEquals(1, builder06.patch());
                Assert.assertEquals("extra", builder06.extra());
                Assert.assertNull(builder06.metadata());

                PluginVersion version = builder06.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(0, builder07.major());
                Assert.assertEquals(0, builder07.minor());
                Assert.assertEquals(1, builder07.patch());
                Assert.assertEquals("extra", builder07.extra());
                Assert.assertEquals("metadata", builder07.metadata());

                PluginVersion version = builder07.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(0, builder08.major());
                Assert.assertEquals(1, builder08.minor());
                Assert.assertEquals(0, builder08.patch());
                Assert.assertNull(builder08.extra());
                Assert.assertNull(builder08.metadata());

                PluginVersion version = builder08.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(0, builder09.major());
                Assert.assertEquals(1, builder09.minor());
                Assert.assertEquals(0, builder09.patch());
                Assert.assertNull(builder09.extra());
                Assert.assertEquals("metadata", builder09.metadata());

                PluginVersion version = builder09.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(0, builder10.major());
                Assert.assertEquals(1, builder10.minor());
                Assert.assertEquals(0, builder10.patch());
                Assert.assertEquals("extra", builder10.extra());
                Assert.assertNull(builder10.metadata());

                PluginVersion version = builder10.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(0, builder11.major());
                Assert.assertEquals(1, builder11.minor());
                Assert.assertEquals(0, builder11.patch());
                Assert.assertEquals("extra", builder11.extra());
                Assert.assertEquals("metadata", builder11.metadata());

                PluginVersion version = builder11.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(0, builder12.major());
                Assert.assertEquals(1, builder12.minor());
                Assert.assertEquals(1, builder12.patch());
                Assert.assertNull(builder12.extra());
                Assert.assertNull(builder12.metadata());

                PluginVersion version = builder12.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(0, builder13.major());
                Assert.assertEquals(1, builder13.minor());
                Assert.assertEquals(1, builder13.patch());
                Assert.assertNull(builder13.extra());
                Assert.assertEquals("metadata", builder13.metadata());

                PluginVersion version = builder13.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(0, builder14.major());
                Assert.assertEquals(1, builder14.minor());
                Assert.assertEquals(1, builder14.patch());
                Assert.assertEquals("extra", builder14.extra());
                Assert.assertNull(builder14.metadata());

                PluginVersion version = builder14.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(0, builder15.major());
                Assert.assertEquals(1, builder15.minor());
                Assert.assertEquals(1, builder15.patch());
                Assert.assertEquals("extra", builder15.extra());
                Assert.assertEquals("metadata", builder15.metadata());

                PluginVersion version = builder15.build();

                Assert.assertEquals(0, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(1, builder16.major());
                Assert.assertEquals(0, builder16.minor());
                Assert.assertEquals(0, builder16.patch());
                Assert.assertNull(builder16.extra());
                Assert.assertNull(builder16.metadata());

                PluginVersion version = builder16.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(1, builder17.major());
                Assert.assertEquals(0, builder17.minor());
                Assert.assertEquals(0, builder17.patch());
                Assert.assertNull(builder17.extra());
                Assert.assertEquals("metadata", builder17.metadata());

                PluginVersion version = builder17.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(1, builder18.major());
                Assert.assertEquals(0, builder18.minor());
                Assert.assertEquals(0, builder18.patch());
                Assert.assertEquals("extra", builder18.extra());
                Assert.assertNull(builder18.metadata());

                PluginVersion version = builder18.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(1, builder19.major());
                Assert.assertEquals(0, builder19.minor());
                Assert.assertEquals(0, builder19.patch());
                Assert.assertEquals("extra", builder19.extra());
                Assert.assertEquals("metadata", builder19.metadata());

                PluginVersion version = builder19.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(1, builder20.major());
                Assert.assertEquals(0, builder20.minor());
                Assert.assertEquals(1, builder20.patch());
                Assert.assertNull(builder20.extra());
                Assert.assertNull(builder20.metadata());

                PluginVersion version = builder20.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(1, builder21.major());
                Assert.assertEquals(0, builder21.minor());
                Assert.assertEquals(1, builder21.patch());
                Assert.assertNull(builder21.extra());
                Assert.assertEquals("metadata", builder21.metadata());

                PluginVersion version = builder21.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(1, builder22.major());
                Assert.assertEquals(0, builder22.minor());
                Assert.assertEquals(1, builder22.patch());
                Assert.assertEquals("extra", builder22.extra());
                Assert.assertNull(builder22.metadata());

                PluginVersion version = builder22.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(1, builder23.major());
                Assert.assertEquals(0, builder23.minor());
                Assert.assertEquals(1, builder23.patch());
                Assert.assertEquals("extra", builder23.extra());
                Assert.assertEquals("metadata", builder23.metadata());

                PluginVersion version = builder23.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(0, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(1, builder24.major());
                Assert.assertEquals(1, builder24.minor());
                Assert.assertEquals(0, builder24.patch());
                Assert.assertNull(builder24.extra());
                Assert.assertNull(builder24.metadata());

                PluginVersion version = builder24.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(1, builder25.major());
                Assert.assertEquals(1, builder25.minor());
                Assert.assertEquals(0, builder25.patch());
                Assert.assertNull(builder25.extra());
                Assert.assertEquals("metadata", builder25.metadata());

                PluginVersion version = builder25.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(1, builder26.major());
                Assert.assertEquals(1, builder26.minor());
                Assert.assertEquals(0, builder26.patch());
                Assert.assertEquals("extra", builder26.extra());
                Assert.assertNull(builder26.metadata());

                PluginVersion version = builder26.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(1, builder27.major());
                Assert.assertEquals(1, builder27.minor());
                Assert.assertEquals(0, builder27.patch());
                Assert.assertEquals("extra", builder27.extra());
                Assert.assertEquals("metadata", builder27.metadata());

                PluginVersion version = builder27.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(0, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(1, builder28.major());
                Assert.assertEquals(1, builder28.minor());
                Assert.assertEquals(1, builder28.patch());
                Assert.assertNull(builder28.extra());
                Assert.assertNull(builder28.metadata());

                PluginVersion version = builder28.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(1, builder29.major());
                Assert.assertEquals(1, builder29.minor());
                Assert.assertEquals(1, builder29.patch());
                Assert.assertNull(builder29.extra());
                Assert.assertEquals("metadata", builder29.metadata());

                PluginVersion version = builder29.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertNull(version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }

            {
                Assert.assertEquals(1, builder30.major());
                Assert.assertEquals(1, builder30.minor());
                Assert.assertEquals(1, builder30.patch());
                Assert.assertEquals("extra", builder30.extra());
                Assert.assertNull(builder30.metadata());

                PluginVersion version = builder30.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertNull(version.metadata());
            }

            {
                Assert.assertEquals(1, builder31.major());
                Assert.assertEquals(1, builder31.minor());
                Assert.assertEquals(1, builder31.patch());
                Assert.assertEquals("extra", builder31.extra());
                Assert.assertEquals("metadata", builder31.metadata());

                PluginVersion version = builder31.build();

                Assert.assertEquals(1, version.major());
                Assert.assertEquals(1, version.minor());
                Assert.assertEquals(1, version.patch());
                Assert.assertEquals("extra", version.extra());
                Assert.assertEquals("metadata", version.metadata());
            }
        }

        /**
         * Tests {@link org.basinmc.faucet.plugin.PluginVersion#builder(PluginVersion)}.
         */
        @Test
        public void testCopy() {
            PluginVersion version = PluginVersion.builder().major(1).minor(1).patch(1).extra("test").metadata("test").build();
            PluginVersion.Builder builder = PluginVersion.builder(version);

            Assert.assertEquals(version.major(), builder.major());
            Assert.assertEquals(version.minor(), builder.minor());
            Assert.assertEquals(version.patch(), builder.patch());
            Assert.assertEquals(version.extra(), builder.extra());
            Assert.assertEquals(version.metadata(), builder.metadata());
        }

        /**
         * Tests {@link org.basinmc.faucet.plugin.PluginVersion.Builder#reset()}
         */
        @Test
        public void testReset() {
            PluginVersion.Builder builder = PluginVersion.builder().major(1).minor(1).patch(1).extra("test").metadata("test");

            Assert.assertEquals(1, builder.major());
            Assert.assertEquals(1, builder.minor());
            Assert.assertEquals(1, builder.patch());
            Assert.assertEquals("test", builder.extra());
            Assert.assertEquals("test", builder.metadata());

            builder.reset();

            Assert.assertEquals(0, builder.major());
            Assert.assertEquals(0, builder.minor());
            Assert.assertEquals(0, builder.patch());
            Assert.assertNull(builder.extra());
            Assert.assertNull(builder.metadata());
        }
    }
}

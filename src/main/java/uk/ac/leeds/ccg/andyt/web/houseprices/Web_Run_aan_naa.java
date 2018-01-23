/*
 * Copyright 2017 Andy Turner, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.andyt.web.houseprices;

import java.util.Iterator;
import java.util.TreeSet;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import uk.ac.leeds.ccg.andyt.generic.data.Generic_UKPostcode_Handler;

/**
 * Class for formatting postcodes of the an_naa format.
 */
public class Web_Run_aan_naa extends Web_AbstractRun {

    /**
     * A reference to tZooplaHousepriceScraper.getAtoZ_not_IJZ() for
     * convenience.
     */
    private TreeSet<String> _AtoZ_not_IJZ;

    /**
     * @param tZooplaHousepriceScraper The Web_ZooplaHousepriceScraper
     * @param restart This is expected to be true if restarting a partially
     * completed run and false otherwise.
     */
    public Web_Run_aan_naa(
            Web_ZooplaHousepriceScraper tZooplaHousepriceScraper,
            boolean restart) {
        init(tZooplaHousepriceScraper, restart);
        this._AtoZ_not_IJZ = Generic_UKPostcode_Handler.get_AtoZ_not_IJZ();
    }

    @Override
    public void run() {
        if (restart == false) {
            formatNew();
        } else {
            if (firstpartPostcode.length() > 1) {
                throw new NotImplementedException();
            }
            // Initialise output files
            String filenamepart = "_Houseprices_" + firstpartPostcode + "an";
            String[] postcodeForRestart = getPostcodeForRestart(
                    getType(), filenamepart);
            if (postcodeForRestart == null) {
                formatNew();
            } else {
                if (postcodeForRestart[0] != null) {
                    String a1Restart = postcodeForRestart[0].substring(1, 2);
                    int n0Restart = Integer.valueOf(postcodeForRestart[0].substring(2, 3));
                    int counter = 0;
                    int numberOfHousepriceRecords = 0;
                    int numberOfPostcodesWithHousepriceRecords = 0;
                    String _NAAString;
                    int _int0;
                    // Initialise output files
                    initialiseOutputs(getType(), filenamepart);
                    // Process
                    Iterator<String> _AtoZ_not_IJZ_Iterator0;
                    Iterator<String> _NAA_Iterator;
                    String a1;
                    boolean n0Restarter = false;
                    boolean a1Restarter = false;
                    boolean secondPartPostcodeRestarter = false;
                    _AtoZ_not_IJZ_Iterator0 = _AtoZ_not_IJZ.iterator();
                    while (_AtoZ_not_IJZ_Iterator0.hasNext()) {
                        a1 = (String) _AtoZ_not_IJZ_Iterator0.next();
                        if (!a1Restarter) {
                            if (a1.equalsIgnoreCase(a1Restart)) {
                                a1Restarter = true;
                            }
                        } else {
                            for (int n0 = 0; n0 < 10; n0++) {
                                if (!n0Restarter) {
                                    if (n0 == n0Restart) {
                                        n0Restarter = true;
                                    }
                                } else {
                                    String completeFirstPartPostcode = firstpartPostcode
                                            + a1 + Integer.toString(n0);
                                    String aURLString0 = url + completeFirstPartPostcode;
                                    checkRequestRate();
                                    if (tZooplaHousepriceScraper.isReturningOutcode(completeFirstPartPostcode, aURLString0)) {
                                        _NAA_Iterator = _NAA.iterator();
                                        while (_NAA_Iterator.hasNext()) {
                                            _NAAString = (String) _NAA_Iterator.next();
                                            if (!secondPartPostcodeRestarter) {
                                                if (_NAAString.equalsIgnoreCase(postcodeForRestart[1])) {
                                                    secondPartPostcodeRestarter = true;
                                                }
                                            } else {
                                                String aURLString = aURLString0 + "-" + _NAAString;
                                                _int0 = tZooplaHousepriceScraper.writeHouseprices(
                                                        outPR,
                                                        logPR,
                                                        sharedLogPR,
                                                        aURLString,
                                                        firstpartPostcode + a1 + Integer.toString(n0),
                                                        _NAAString,
                                addressAdditionalPropertyDetails);
                                                counter++;
                                                numberOfHousepriceRecords += _int0;
                                                if (_int0 > 0) {
                                                    numberOfPostcodesWithHousepriceRecords++;
                                                }
                                            }
                                        }
                                    } else {
                                        Web_ZooplaHousepriceScraper.updateLog(
                                                logPR,
                                                sharedLogPR,
                                                completeFirstPartPostcode);
                                    }
                                }
//                                System.out.println(getReportString(
//                                        counter,
//                                        numberOfHousepriceRecords,
//                                        numberOfPostcodesWithHousepriceRecords));
                            }
                        }
                    }
                    // Final reporting
                    finalise(counter, numberOfHousepriceRecords, numberOfPostcodesWithHousepriceRecords);
                }
            }
        }
    }

    private void formatNew() {
        int counter = 0;
        int numberOfHousepriceRecords = 0;
        int numberOfPostcodesWithHousepriceRecords = 0;
        String _NAAString;
        int _int0;
        if (firstpartPostcode.length() == 3) {
            // Initialise output files
            String filenamepart = "_Houseprices_" + firstpartPostcode;
            initialiseOutputs("aan", filenamepart);
            // Process
            String aURLString0 = url + firstpartPostcode;
            checkRequestRate();
            if (tZooplaHousepriceScraper.isReturningOutcode(firstpartPostcode, aURLString0)) {
                Iterator<String> _NAA_Iterator;
                _NAA_Iterator = _NAA.iterator();
                while (_NAA_Iterator.hasNext()) {
                    _NAAString = (String) _NAA_Iterator.next();
                    String aURLString = aURLString0 + "-" + _NAAString;
                    _int0 = tZooplaHousepriceScraper.writeHouseprices(
                            outPR,
                            logPR,
                            sharedLogPR,
                            aURLString,
                            firstpartPostcode,
                            _NAAString,
                                addressAdditionalPropertyDetails);
                    counter++;
                    numberOfHousepriceRecords += _int0;
                    if (_int0 > 0) {
                        numberOfPostcodesWithHousepriceRecords++;
                    }
                }
            } else {
                Web_ZooplaHousepriceScraper.updateLog(
                        logPR,
                        sharedLogPR,
                        firstpartPostcode);
            }
//                System.out.println(getReportString(
//                        counter,
//                        numberOfHousepriceRecords,
//                        numberOfPostcodesWithHousepriceRecords));
        } else {
            if (firstpartPostcode.length() == 2) {
                // Initialise output files
                String filenamepart = "_Houseprices_" + firstpartPostcode + "n";
                initialiseOutputs("aan", filenamepart);
                // Process
                for (int n0 = 0; n0 < 10; n0++) {
                    String completeFirstPartPostcode = firstpartPostcode + Integer.toString(n0);
                    String aURLString0 = url + completeFirstPartPostcode;
                    checkRequestRate();
                        if (tZooplaHousepriceScraper.isReturningOutcode(completeFirstPartPostcode, aURLString0)) {
                            Iterator<String> _NAA_Iterator = _NAA.iterator();
                            while (_NAA_Iterator.hasNext()) {
                                _NAAString = (String) _NAA_Iterator.next();
                                String aURLString = aURLString0 + "-" + _NAAString;
                                _int0 = tZooplaHousepriceScraper.writeHouseprices(
                                        outPR,
                                        logPR,
                                        sharedLogPR,
                                        aURLString,
                                        firstpartPostcode + Integer.toString(n0),
                                        _NAAString,
                                addressAdditionalPropertyDetails);
                                counter++;
                                numberOfHousepriceRecords += _int0;
                                if (_int0 > 0) {
                                    numberOfPostcodesWithHousepriceRecords++;
                                }
                            }
                        } else {
                            Web_ZooplaHousepriceScraper.updateLog(
                                    logPR,
                                    sharedLogPR,
                                    completeFirstPartPostcode);
                        }
//                    System.out.println(getReportString(
//                            counter,
//                            numberOfHousepriceRecords,
//                            numberOfPostcodesWithHousepriceRecords));
                    }
                }   else {
                // Initialise output files
                String filenamepart = "_Houseprices_" + firstpartPostcode + "an";
                initialiseOutputs("aan", filenamepart);
                // Process
                Iterator<String> _AtoZ_not_IJZ_Iterator0;
                String a1;
                String a1LowerCase;
                _AtoZ_not_IJZ_Iterator0 = _AtoZ_not_IJZ.iterator();
                while (_AtoZ_not_IJZ_Iterator0.hasNext()) {
                    a1 = (String) _AtoZ_not_IJZ_Iterator0.next();
                    for (int n0 = 0; n0 < 10; n0++) {
                        String completeFirstPartPostcode = firstpartPostcode
                                + a1 + Integer.toString(n0);
                        String aURLString0 = url + completeFirstPartPostcode;
                        checkRequestRate();
                        if (tZooplaHousepriceScraper.isReturningOutcode(completeFirstPartPostcode, aURLString0)) {
                                Iterator<String> _NAA_Iterator = _NAA.iterator();
                                while (_NAA_Iterator.hasNext()) {
                                    _NAAString = (String) _NAA_Iterator.next();
                                    String aURLString = aURLString0 + "-" + _NAAString;
                                    _int0 = tZooplaHousepriceScraper.writeHouseprices(
                                            outPR,
                                            logPR,
                                            sharedLogPR,
                                            aURLString,
                                            firstpartPostcode + a1 + Integer.toString(n0),
                                            _NAAString,
                                addressAdditionalPropertyDetails);
                                    counter++;
                                    numberOfHousepriceRecords += _int0;
                                    if (_int0 > 0) {
                                        numberOfPostcodesWithHousepriceRecords++;
                                    }
                                }
                            } else {
                                Web_ZooplaHousepriceScraper.updateLog(
                                        logPR,
                                        sharedLogPR,
                                        completeFirstPartPostcode);
                            }
                        }
//                    System.out.println(getReportString(
//                            counter,
//                            numberOfHousepriceRecords,
//                            numberOfPostcodesWithHousepriceRecords));
                    }
                }
            }
            // Final reporting
            finalise(counter, numberOfHousepriceRecords, numberOfPostcodesWithHousepriceRecords);
        }
    }

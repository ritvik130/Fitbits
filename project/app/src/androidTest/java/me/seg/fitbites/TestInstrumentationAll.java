package me.seg.fitbites;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import android.util.Log;

import androidx.constraintlayout.motion.widget.Debug;

import me.seg.fitbites.data.Days;
import me.seg.fitbites.data.Difficulty;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.FitClassType;
import me.seg.fitbites.data.GymMember;
import me.seg.fitbites.data.Instructor;
import me.seg.fitbites.data.UserData;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;

public class TestInstrumentationAll {

    //IMPORTANT, BEFORE RUNNING TESTS, ENSURE FIREBASE IS CLEAN AND DOES NOT CONTAIN ANY OF THE ACCOUNTS, OR
    //DATA (all data is automatically deleted at end)

    //Note: all test are setup to run slowly and take more time than expected
    //this is so we are not pinging the firebase servers too quickly
    //but also to ensure all data is updated on time and not checked too early
    //testSearchUsers takes the longest at around 20-30 seconds

    //for TA bc of the above warning, if you want to run the unit test, try changing
    //AUTHTEST_USER and AUTHTEST_PASS to other values to ensure it is clean before the test if the test
    //consistently fails
    //reason is bc the email already exists on the database and for test to work, we have to assume
    //user does not yet exist

    //additionally there is a 5th unit test in the class TestJUnitTests

    @Before
    public void setup() {
        //Pre-prepared Required firebase instances
        AuthManager.getInstance();
        FirestoreDatabase.getInstance();
    }

    //Firebase only tests
    //Auth manager test

    //dummy user
    private static final String AUTHTEST_USER = "randomuser@gmail.com";
    private static final String AUTHTEST_PASS = "random123s*";
    private static GymMember g = null;
    @Test
    public void testAuth() throws Exception {

        final Object syncObject = new Object();

        AuthManager.getInstance().createUser(AUTHTEST_USER, AUTHTEST_PASS, new OnTaskComplete<AuthManager.LoginResult>() {
            @Override
            public void onComplete(AuthManager.LoginResult result) {

                assertTrue(result.isSuccessful());

                g = new GymMember(result.getUserData(), "name", "name", "name", "name", "5", AUTHTEST_PASS, AUTHTEST_USER);

                FirestoreDatabase.getInstance().setUserData(g);

                synchronized (syncObject) {
                    syncObject.notify();
                }

            }
        });

        synchronized (syncObject) {
            syncObject.wait();
        }

        AuthManager.getInstance().signoutUser();

        //login as admin
        AuthManager.getInstance().validateUser("admin", "admin123", new OnTaskComplete<AuthManager.LoginResult>() {
            @Override
            public void onComplete(AuthManager.LoginResult result) {

            }
        });

        AuthManager.getInstance().deleteAccount(g);

        synchronized (syncObject) {
            syncObject.wait(10000);
        }

        AuthManager.getInstance().signoutUser();

        AuthManager.getInstance().validateUser(AUTHTEST_USER, AUTHTEST_PASS, new OnTaskComplete<AuthManager.LoginResult>() {
            @Override
            public void onComplete(AuthManager.LoginResult result) {
                assertFalse(result.isSuccessful());
                synchronized (syncObject) {
                    syncObject.notify();
                }
            }
        });

        //waits for all async calls to finish
        synchronized (syncObject) {
            syncObject.wait();
        }
    }

    @Test
    public void testDatabaseFitClass() throws Exception {

        final Object syncObject = new Object();

        //test fitclasstype and fitclass
        FitClassType fct = FitClassType.createFitClassType("random test", "random test");
        FitClass fc = FitClass.createClass(fct);
        fc.setDate(Days.MONDAY);
        fc.setTime(FitClass.convertTime(8, 30));
        fc.setDifficulty(Difficulty.EXPERIENCED);

        FirestoreDatabase.getInstance().setFitClassType(fct);
        FirestoreDatabase.getInstance().setFitClass(fc);


        //test type
        FirestoreDatabase.getInstance().getFitClass(fc.getUid(), new OnTaskComplete<FitClass>() {
            @Override
            public void onComplete(FitClass result) {
                assertNotNull(result);

                //verify equals
                assertEquals(fc.getUid(), result.getUid());
                assertEquals(fc.getDate(), result.getDate());
                assertEquals(fc.getTime(), result.getTime());
                assertEquals(fc.getDifficulty(), result.getDifficulty());

                FirestoreDatabase.getInstance().deleteFitClass(fc);

                FirestoreDatabase.getInstance().getFitClass(fc.getUid(), new OnTaskComplete<FitClass>() {
                    @Override
                    public void onComplete(FitClass result) {
                        assertNull(result);
                        synchronized (syncObject) {
                            syncObject.notify();
                        }
                    }
                });
            }
        });

        synchronized (syncObject) {
            syncObject.wait();
            //wait another 5 seconds before next test
            syncObject.wait(5000);
        }
    }

    @Test
    public void testDatabaseFitClassType() throws Exception {

        final Object syncObject = new Object();

        FitClassType fct = FitClassType.createFitClassType("random test", "random test");
        FirestoreDatabase.getInstance().getFitClassType(fct.getUid(), new OnTaskComplete<FitClassType>() {
            @Override
            public void onComplete(FitClassType result) {
                assertNotNull(result);

                //verify equals
                assertEquals(fct.getClassName(), result.getClassName());
                assertEquals(fct.getUid(), result.getUid());
                assertEquals(fct.getDescription(), result.getDescription());

                FirestoreDatabase.getInstance().deleteFitClassType(fct);

                //check if data still in database
                FirestoreDatabase.getInstance().getFitClassType(fct.getUid(), new OnTaskComplete<FitClassType>() {
                    @Override
                    public void onComplete(FitClassType result) {
                        assertNull(result);
                        synchronized (syncObject) {
                            syncObject.notify();
                        }
                    }
                });
            }
        });

        synchronized (syncObject) {
            syncObject.wait();
            //wait another 5 seconds before next test
            syncObject.wait(5000);
        }
    }

    private static final String testUserSuffix = "zzztestzzz";

    @Test
    public void testSearchUsers() throws Exception {

        final Object syncObject = new Object();

        //create bunch of users and put into database
        GymMember[] g = new GymMember[] {
                new GymMember("testg0", testUserSuffix, testUserSuffix, testUserSuffix + "g0", testUserSuffix, "5", testUserSuffix, testUserSuffix + "g0"),
                new GymMember("testg1", testUserSuffix, testUserSuffix, testUserSuffix + "g1", testUserSuffix, "5", testUserSuffix, testUserSuffix + "g1"),
                new GymMember("testg2", testUserSuffix, testUserSuffix, testUserSuffix + "g2", testUserSuffix, "5", testUserSuffix, testUserSuffix + "g2")
        };

        Instructor[] i = new Instructor[] {
                new Instructor("testi0", testUserSuffix, testUserSuffix, testUserSuffix + "i0", testUserSuffix, "5", testUserSuffix, testUserSuffix + "i0"),
                new Instructor("testi1", testUserSuffix, testUserSuffix, testUserSuffix + "i1", testUserSuffix, "5", testUserSuffix, testUserSuffix + "i1"),
                new Instructor("testi2", testUserSuffix, testUserSuffix, testUserSuffix + "i2", testUserSuffix, "5", testUserSuffix, testUserSuffix + "i2")
        };

        for(GymMember gm : g) {
            FirestoreDatabase.getInstance().setUserData(gm);
        }

        for(Instructor im : i) {
            FirestoreDatabase.getInstance().setUserData(im);
        }

        //wait enough time till all data is in database
        synchronized (syncObject) {
            syncObject.wait(5000);
        }

        UserData.searchUser(testUserSuffix, new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(6, result.length);

            }
        });

        UserData.searchUser(testUserSuffix + "g", new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(3, result.length);
            }
        });

        UserData.searchUser(testUserSuffix + "i", new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(3, result.length);
            }
        });

        UserData.searchUser(testUserSuffix + "g0", new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(1, result.length);

                assertEquals(g[0].getUid(), result[0].getUid());
            }
        });

        UserData.searchUser(testUserSuffix + "i0", new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(1, result.length);

                assertEquals(i[0].getUid(), result[0].getUid());
            }
        });

        //waits 10 seconds to complete all test async
        synchronized (syncObject) {
            syncObject.wait(5000);
        }

        for(GymMember gm : g) {
            FirestoreDatabase.getInstance().deleteUserData(gm);
        }

        for(Instructor im : i) {
            FirestoreDatabase.getInstance().deleteUserData(im);
        }

        //wait enough time till all data is in database
        synchronized (syncObject) {
            syncObject.wait(5000);
        }

        //verify deletion
        UserData.searchUser(testUserSuffix, new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(0, result.length);
            }
        });

        UserData.searchUser(testUserSuffix + "g", new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(0, result.length);
            }
        });

        UserData.searchUser(testUserSuffix + "i", new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(0, result.length);
            }
        });

        UserData.searchUser(testUserSuffix + "g0", new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(0, result.length);
            }
        });

        UserData.searchUser(testUserSuffix + "i0", new OnTaskComplete<UserData[]>() {
            @Override
            public void onComplete(UserData[] result) {
                assertEquals(0, result.length);
            }
        });

        //waits 10 seconds to complete all test async
        synchronized (syncObject) {
            syncObject.wait(5000);
        }

    }

}

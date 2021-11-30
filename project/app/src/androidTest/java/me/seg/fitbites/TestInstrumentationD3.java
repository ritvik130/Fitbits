package me.seg.fitbites;

import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import static org.junit.Assert.*;

import android.view.View;

import me.seg.fitbites.data.Days;
import me.seg.fitbites.data.Difficulty;
import me.seg.fitbites.data.FitClass;
import me.seg.fitbites.data.GymMember;
import me.seg.fitbites.data.UserData;
import me.seg.fitbites.firebase.AuthManager;
import me.seg.fitbites.firebase.FirestoreDatabase;
import me.seg.fitbites.firebase.OnTaskComplete;
import me.seg.fitbites.layouts.member.MemberEnrollClass;
import me.seg.fitbites.layouts.member.MemberWelcomeActivity;

//Instrumentation test specifically for deliverable 3
@RunWith(AndroidJUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestInstrumentationD3 {

    //before running test,
    //ensure at least one class is in database, and that it is on sunday

    //test to ensure fitclass can push to database without errors
    //due to the addition of class lists
    //Reason for the test is sometimes firebase does not accept the parsing for certain elements
    private FitClass fitClassExperiment;
    private GymMember testMember;

    @Before
    public void beforeTests() {
        testMember = new GymMember("testuid", "", "", "","", "5", "", "");
    }

    @Test
    public void testAFitClassEnrollment() throws Exception {
        //grab first fitclass in database

        final Object syncObj = new Object();

        //get random class from database to test enrollment method
        FirestoreDatabase.getInstance().getAvailableClasses(new OnTaskComplete<FitClass[]>() {
            @Override
            public void onComplete(FitClass[] result) {
                fitClassExperiment = result[0];
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        int expSize = fitClassExperiment.getMemberListSize();

        fitClassExperiment.enrollMember(testMember);

        fitClassExperiment.updateClass();

        //allow for update
        synchronized (syncObj) {
            syncObj.wait(2000);
        }

        //get class
        FirestoreDatabase.getInstance().getFitClass(fitClassExperiment.getUid(), new OnTaskComplete<FitClass>() {
            @Override
            public void onComplete(FitClass result) {
                fitClassExperiment = result;
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        //assert that enrollment was successful
        assertEquals(expSize + 1, fitClassExperiment.getMemberListSize());

        fitClassExperiment.unenrollMember(testMember);

        fitClassExperiment.updateClass();

        //allow for update
        synchronized (syncObj) {
            syncObj.wait(2000);
        }

        //get class
        FirestoreDatabase.getInstance().getFitClass(fitClassExperiment.getUid(), new OnTaskComplete<FitClass>() {
            @Override
            public void onComplete(FitClass result) {
                fitClassExperiment = result;
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        //assert that enrollment was successful
        assertEquals(expSize, fitClassExperiment.getMemberListSize());
    }

    @Test
    public void testBUserEnrollment() throws Exception {

        final Object syncObj = new Object();

        //get random class from database to test enrollment method
        FirestoreDatabase.getInstance().getAvailableClasses(new OnTaskComplete<FitClass[]>() {
            @Override
            public void onComplete(FitClass[] result) {
                fitClassExperiment = result[0];
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        //upload user to database
        FirestoreDatabase.getInstance().setUserData(testMember);

        synchronized (syncObj) {
            syncObj.wait(2000);
        }

        //enroll
        testMember.enrollClass(fitClassExperiment);

        FirestoreDatabase.getInstance().setUserData(testMember);

        synchronized (syncObj) {
            syncObj.wait(2000);
        }

        FirestoreDatabase.getInstance().getUserData(testMember.getUid(), new OnTaskComplete<UserData>() {
            @Override
            public void onComplete(UserData result) {
                testMember = (GymMember) result;
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        assertEquals(1, testMember.getEnrolledClasses().size());
        assertEquals(fitClassExperiment.getUid(), testMember.getEnrolledClasses().get(0));

        testMember.unenrollClass(fitClassExperiment);

        FirestoreDatabase.getInstance().setUserData(testMember);

        synchronized (syncObj) {
            syncObj.wait(2000);
        }

        FirestoreDatabase.getInstance().getUserData(testMember.getUid(), new OnTaskComplete<UserData>() {
            @Override
            public void onComplete(UserData result) {
                testMember = (GymMember) result;
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        assertEquals(0, testMember.getEnrolledClasses().size());

    }

    @Test
    public void testCUserCollisionCheck() throws Exception {
        final Object syncObj = new Object();

        //create Test Classes
        FitClass c1 = new FitClass("test1", "test0", "test0", Days.MONDAY, 100,10, Difficulty.BEGINNER);
        c1.setEndTime(110);
        FitClass c2 = new FitClass("test2", "test0", "test0", Days.MONDAY, 90,10, Difficulty.BEGINNER);
        c2.setEndTime(105);
        FitClass c3 = new FitClass("test3", "test0", "test0", Days.MONDAY, 105,10, Difficulty.BEGINNER);
        c3.setEndTime(120);
        FitClass c4 = new FitClass("test4", "test0", "test0", Days.MONDAY, 101,10, Difficulty.BEGINNER);
        c4.setEndTime(109);
        FitClass c5 = new FitClass("test5", "test0", "test0", Days.MONDAY, 90,10, Difficulty.BEGINNER);
        c5.setEndTime(120);
        FitClass c6 = new FitClass("test6", "test0", "test0", Days.TUESDAY, 90,10, Difficulty.BEGINNER);
        c6.setEndTime(120);
        FitClass c7 = new FitClass("test7", "test0", "test0", Days.MONDAY, 60,10, Difficulty.BEGINNER);
        c7.setEndTime(70);

        //add all to database
        FirestoreDatabase.getInstance().setFitClass(c1);
        FirestoreDatabase.getInstance().setFitClass(c2);
        FirestoreDatabase.getInstance().setFitClass(c3);
        FirestoreDatabase.getInstance().setFitClass(c4);
        FirestoreDatabase.getInstance().setFitClass(c5);
        FirestoreDatabase.getInstance().setFitClass(c6);
        FirestoreDatabase.getInstance().setFitClass(c7);

        synchronized (syncObj) {
            syncObj.wait(2000);
        }

        testMember.enrollClass(c1);

        testMember.checkClassCollision(c2, new OnTaskComplete<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                assertTrue(result);
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        testMember.checkClassCollision(c3, new OnTaskComplete<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                assertTrue(result);
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        testMember.checkClassCollision(c4, new OnTaskComplete<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                assertTrue(result);
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        testMember.checkClassCollision(c5, new OnTaskComplete<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                assertTrue(result);
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        testMember.checkClassCollision(c6, new OnTaskComplete<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                assertFalse(result);
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        testMember.checkClassCollision(c7, new OnTaskComplete<Boolean>() {
            @Override
            public void onComplete(Boolean result) {
                assertFalse(result);
                synchronized (syncObj) {
                    syncObj.notify();
                }
            }
        });

        synchronized (syncObj) {
            syncObj.wait();
        }

        FirestoreDatabase.getInstance().deleteUserData(testMember);

        FirestoreDatabase.getInstance().deleteFitClass(c1);
        FirestoreDatabase.getInstance().deleteFitClass(c2);
        FirestoreDatabase.getInstance().deleteFitClass(c3);
        FirestoreDatabase.getInstance().deleteFitClass(c4);
        FirestoreDatabase.getInstance().deleteFitClass(c5);
        FirestoreDatabase.getInstance().deleteFitClass(c6);
        FirestoreDatabase.getInstance().deleteFitClass(c7);

    }

    private String GM_LOGIN = "abc@email.com";
    private String GM_PASS = "abc123a";

    private GymMember curMember;

    @Test
    public void testFitClassMethodCheck() throws Exception {
        FitClass c = new FitClass("test1", "test0", "test0", Days.MONDAY, 100,10, Difficulty.BEGINNER);
        c.enrollMember(testMember);
        assertEquals(testMember.getUid(), c.getMemberIdList().get(0));
        assertEquals(1, c.getMemberListSize());

        c.unenrollMember(testMember);
        assertEquals(0, c.getMemberListSize());
    }

    @Test
    public void testGymMemberMethodCheck() {
        FitClass c = new FitClass("test1", "test0", "test0", Days.MONDAY, 100,10, Difficulty.BEGINNER);
        testMember.enrollClass(c);

        assertEquals(c.getUid(), testMember.getEnrolledClasses().get(0));
        assertEquals(1, testMember.getEnrolledClasses().size());

        testMember.unenrollClass(c);
        assertEquals(0, testMember.getEnrolledClasses().size());
    }

}

/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.android.services.telephony.common;

import com.google.common.collect.ImmutableMap;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

import com.android.internal.telephony.PhoneConstants;

/**
 * Class object used across CallHandlerService APIs.
 * Describes a single call and its state.
 */
final public class Call implements Parcelable {

    public static final int INVALID_CALL_ID = -1;

    /* Defines different states of this call */
    public static class State {
        public static final int INVALID = 0;

        // The call is idle.  Nothing active.
        public static final int IDLE = 1;

        // There is an active call.
        public static final int ACTIVE = 2;

        // A normal incoming phone call.
        public static final int INCOMING = 3;

        // An incoming phone call while another call is active.
        public static final int CALL_WAITING = 4;

        // A Mobile-originating (MO) call. This call is dialing out.
        public static final int DIALING = 5;

        // An active phone call placed on hold.
        public static final int ONHOLD = 6;

        // State after a call disconnects.
        public static final int DISCONNECTED = 7;
    }

    /**
     * Copy of states found in Connection object since Connection object is not available to the UI
     * code.
     * TODO: Consider cutting this down to only the types used by the UI.
     * TODO: Consider adding a CUSTOM cause type and a customDisconnect member variable to
     *       the Call object.  This would allow OEMs to extend the cause list without
     *       needing to alter our implementation.
     */
    public enum DisconnectCause {
        NOT_DISCONNECTED,               /* has not yet disconnected */
        INCOMING_MISSED,                /* an incoming call that was missed and never answered */
        NORMAL,                         /* normal; remote */
        LOCAL,                          /* normal; local hangup */
        BUSY,                           /* outgoing call to busy line */
        CONGESTION,                     /* outgoing call to congested network */
        MMI,                            /* not presently used; dial() returns null */
        INVALID_NUMBER,                 /* invalid dial string */
        NUMBER_UNREACHABLE,             /* cannot reach the peer */
        SERVER_UNREACHABLE,             /* cannot reach the server */
        INVALID_CREDENTIALS,            /* invalid credentials */
        OUT_OF_NETWORK,                 /* calling from out of network is not allowed */
        SERVER_ERROR,                   /* server error */
        TIMED_OUT,                      /* client timed out */
        LOST_SIGNAL,
        LIMIT_EXCEEDED,                 /* eg GSM ACM limit exceeded */
        INCOMING_REJECTED,              /* an incoming call that was rejected */
        POWER_OFF,                      /* radio is turned off explicitly */
        OUT_OF_SERVICE,                 /* out of service */
        ICC_ERROR,                      /* No ICC, ICC locked, or other ICC error */
        CALL_BARRED,                    /* call was blocked by call barring */
        FDN_BLOCKED,                    /* call was blocked by fixed dial number */
        CS_RESTRICTED,                  /* call was blocked by restricted all voice access */
        CS_RESTRICTED_NORMAL,           /* call was blocked by restricted normal voice access */
        CS_RESTRICTED_EMERGENCY,        /* call was blocked by restricted emergency voice access */
        UNOBTAINABLE_NUMBER,            /* Unassigned number (3GPP TS 24.008 table 10.5.123) */
        CDMA_LOCKED_UNTIL_POWER_CYCLE,  /* MS is locked until next power cycle */
        CDMA_DROP,
        CDMA_INTERCEPT,                 /* INTERCEPT order received, MS state idle entered */
        CDMA_REORDER,                   /* MS has been redirected, call is cancelled */
        CDMA_SO_REJECT,                 /* service option rejection */
        CDMA_RETRY_ORDER,               /* requested service is rejected, retry delay is set */
        CDMA_ACCESS_FAILURE,
        CDMA_PREEMPTED,
        CDMA_NOT_EMERGENCY,              /* not an emergency call */
        CDMA_ACCESS_BLOCKED,            /* Access Blocked by CDMA network */
        ERROR_UNSPECIFIED,

        UNKNOWN                         /* Disconnect cause doesn't map to any above */
    }

    private static final Map<Integer, String> STATE_MAP = ImmutableMap.<Integer, String>builder()
            .put(Call.State.ACTIVE, "ACTIVE")
            .put(Call.State.CALL_WAITING, "CALL_WAITING")
            .put(Call.State.DIALING, "DIALING")
            .put(Call.State.IDLE, "IDLE")
            .put(Call.State.INCOMING, "INCOMING")
            .put(Call.State.ONHOLD, "ONHOLD")
            .put(Call.State.INVALID, "INVALID")
            .put(Call.State.DISCONNECTED, "DISCONNECTED")
            .build();

    // Number presentation type for caller id display
    // normal
    public static int PRESENTATION_ALLOWED = PhoneConstants.PRESENTATION_ALLOWED;
    // block by user
    public static int PRESENTATION_RESTRICTED = PhoneConstants.PRESENTATION_RESTRICTED;
    // no specified or unknown by network
    public static int PRESENTATION_UNKNOWN = PhoneConstants.PRESENTATION_UNKNOWN;
    // show pay phone info
    public static int PRESENTATION_PAYPHONE = PhoneConstants.PRESENTATION_PAYPHONE;

    private int mCallId = INVALID_CALL_ID;
    private String mNumber = "";
    private int mState = State.INVALID;
    private int mNumberPresentation = PRESENTATION_ALLOWED;
    private int mCnapNamePresentation = PRESENTATION_ALLOWED;
    private String mCnapName = "";
    private DisconnectCause mDisconnectCause;

    public Call(int callId) {
        mCallId = callId;
    }

    public int getCallId() {
        return mCallId;
    }

    public String getNumber() {
        return mNumber;
    }

    public void setNumber(String number) {
        mNumber = number;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public int getNumberPresentation() {
        return mNumberPresentation;
    }

    public void setNumberPresentation(int presentation) {
        mNumberPresentation = presentation;
    }

    public int getCnapNamePresentation() {
        return mCnapNamePresentation;
    }

    public void setCnapNamePresentation(int presentation) {
        mCnapNamePresentation = presentation;
    }

    public String getCnapName() {
        return mCnapName;
    }

    public void setCnapName(String cnapName) {
        mCnapName = cnapName;
    }

    public DisconnectCause getDisconnectCause() {
        if (mState == State.DISCONNECTED || mState == State.IDLE) {
            return mDisconnectCause;
        }

        return DisconnectCause.NOT_DISCONNECTED;
    }

    public void setDisconnectCause(DisconnectCause cause) {
        mDisconnectCause = cause;
    }

    /**
     * Parcelable implementation
     */

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mCallId);
        dest.writeString(mNumber);
        dest.writeInt(mState);
        dest.writeInt(mNumberPresentation);
        dest.writeInt(mCnapNamePresentation);
        dest.writeString(mCnapName);
        dest.writeString(getDisconnectCause().toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Call> CREATOR
            = new Parcelable.Creator<Call>() {

        public Call createFromParcel(Parcel in) {
            return new Call(in);
        }

        public Call[] newArray(int size) {
            return new Call[size];
        }
    };

    private Call(Parcel in) {
        mCallId = in.readInt();
        mNumber = in.readString();
        mState = in.readInt();
        mNumberPresentation = in.readInt();
        mCnapNamePresentation = in.readInt();
        mCnapName = in.readString();
        mDisconnectCause = DisconnectCause.valueOf(in.readString());
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("callId: ");
        buffer.append(mCallId);
        buffer.append(", state: ");
        buffer.append(STATE_MAP.get(mState));
        buffer.append(", disconnect_cause: ");
        buffer.append(getDisconnectCause().toString());
        return buffer.toString();
    }
}
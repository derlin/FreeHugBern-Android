/*
 * Copyright 2014 Akexorcist
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package ch.derlin.freehugbern.bt;

/**
 * This class contains all the constants used by the BluetoothService
 * <p/>
 * <p/>
 * Other constants: the maximal size of some fields, the intent filter for broadcasts, etc. are also defined.
 * <p/>
 * creation date    01.12.2014
 * context          Projet de semestre Hugginess, EIA-FR, I3 2014-2015
 *
 * @author Lucy Linder
 */
public class BluetoothConstants{

    // ---------------------------------------------------- BluetoothService
    // Constants that indicate the current connection state
    /** Bluetooth turned off **/
    public static final int STATE_TURNED_OFF = -1;
    /** Bluetooth available, but doing nothin **/
    public static final int STATE_NONE = 0;
    /** Trying to connect **/
    public static final int STATE_CONNECTING = 2;
    /** Connected to a remote device **/
    public static final int STATE_CONNECTED = 3;

    /** Intent request code that can be used when requesting the bluetooth service to turn the adapter on **/
    public static final int REQUEST_ENABLE_BT = 385;

    // ----------------------------------------------------

    /** Intent filter for bluetooth local broadcasts. **/
    public static final String BTSERVICE_INTENT_FILTER = "BTService";

    /** Extra which is present in all local broadcast: it identifies the kind of event. **/
    public static final String EXTRA_EVT_TYPE = "extra_evt_type";

    // bt adapter-related events
    /** Event type: bluetooth turned on. **/
    public static final String EVT_BT_TURNED_ON = "evt_turned_on";
    /** Event type: bluetooth turned off. **/
    public static final String EVT_BT_TURNED_OFF = "evt_turned_off";
    // connection-related events
    /** Event type: device connected. **/
    public static final String EVT_CONNECTED = "evt_connected";
    /** Event type: device disconnected. **/
    public static final String EVT_DISCONNECTED = "evt_disconnected";
    /** Event type: could not connect. **/
    public static final String EVT_CONNECTION_FAILED = "evt_connection_failed";
    // data-related events
    /** Event type: data received. Extras: {@link #EVT_EXTRA_DATA} **/
    public static final String EVT_DATA_RECEIVED = "evt_data_received";

    // String extra some events
    /** Extra: data received, String (see {@link #EVT_DATA_RECEIVED})**/
    public static final String EVT_EXTRA_DATA = "extra_data_received";

}

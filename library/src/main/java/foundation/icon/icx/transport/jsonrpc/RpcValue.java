/*
 * Copyright 2018 ICON Foundation
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
 *
 */

package foundation.icon.icx.transport.jsonrpc;

import foundation.icon.icx.data.Address;
import foundation.icon.icx.data.Bytes;
import foundation.icon.icx.data.Hex;

import java.math.BigInteger;

import static foundation.icon.icx.data.Hex.HEX_PREFIX;

/**
 * RpcValue contains a leaf value such as string, bytes, integer, boolean
 */
public class RpcValue implements RpcItem {

    private String value;

    public RpcValue(RpcValue value) {
        this.value = value.asString();
    }

    public RpcValue(Address value) {
        if (value != null) {
            this.value = value.asString();
        }
    }

    public RpcValue(String value) {
        this.value = value;
    }

    public RpcValue(byte[] value) {
        this.value = Hex.toHexString(value, true);
    }

    public RpcValue(BigInteger value) {
        this.value = Hex.toHexString(value, true);
    }

    public RpcValue(Boolean value) {
        this.value = Hex.toHexString(value, true);
    }

    public RpcValue(boolean value) {
        this.value = Hex.toHexString(value, true);
    }

    public RpcValue(Hex value) {
        this.value = value.asString();
    }

    public RpcValue(Bytes value) {
        this.value = value.toString();
    }

    @Override
    public boolean isEmpty() {
        return value == null || value.isEmpty();
    }

    /**
     * Returns the value as string
     *
     * @return the value as string
     */
    @Override
    public String asString() {
        return value;
    }

    /**
     * Returns the value as bytes
     *
     * @return the value as bytes
     */
    @Override
    public byte[] asByteArray() {
        if (!value.startsWith(HEX_PREFIX)) {
            throw new RpcValueException("The value is not hex string.");
        }

        // bytes should be even length of hex string
        if (value.length() % 2 != 0) {
            throw new RpcValueException(
                    "The hex value is not bytes format.");
        }

        return Hex.hexStringToByteArray(value);
    }

    @Override
    public Address asAddress() {
        return new Address(value);
    }

    @Override
    public Bytes asBytes() {
        return new Bytes(value);
    }

    @Override
    public Hex asHex() {
        return new Hex(value);
    }

    /**
     * Returns the value as integer
     *
     * @return the value as integer
     */
    @Override
    public BigInteger asInteger() {
        if (!(value.startsWith(HEX_PREFIX) || value.startsWith('-' + HEX_PREFIX))) {
            throw new RpcValueException("The value is not hex string.");
        }

        try {
            return Hex.toBigInteger(value);
        } catch (NumberFormatException e) {
            throw new RpcValueException("The value is not hex string.");
        }
    }

    /**
     * Returns the value as boolean
     *
     * @return the value as boolean
     */
    @Override
    public boolean asBoolean() {
        switch (value) {
            case "0x0":
                return false;
            case "0x1":
                return true;
            default:
                throw new RpcValueException("The value is not boolean format.");
        }
    }

    @Override
    public String toString() {
        return "RpcValue(" +
                "value=" + value +
                ')';
    }

}

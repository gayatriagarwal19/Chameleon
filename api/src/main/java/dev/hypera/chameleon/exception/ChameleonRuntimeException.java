/*
 * This file is a part of the Chameleon Framework, licensed under the MIT License.
 *
 * Copyright (c) 2021-2023 The Chameleon Framework Authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.hypera.chameleon.exception;

/**
 * Chameleon runtime exception.
 */
public class ChameleonRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -5120195040769782664L;

    /**
     * Chameleon runtime exception constructor.
     */
    public ChameleonRuntimeException() {
        super();
    }

    /**
     * Chameleon runtime exception constructor.
     *
     * @param message Exception message.
     */
    public ChameleonRuntimeException(String message) {
        super(message);
    }

    /**
     * Chameleon runtime exception constructor.
     *
     * @param message Exception message.
     * @param cause   Exception cause.
     */
    public ChameleonRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Chameleon runtime exception constructor.
     *
     * @param cause Exception cause.
     */
    public ChameleonRuntimeException(Throwable cause) {
        super(cause);
    }

    protected ChameleonRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

package com.maproductions.mohamedalaa.shared.core.extensions

import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

inline fun <reified T> T.convertToByteArray(): ByteArray? {
    val bos = ByteArrayOutputStream()
    var byteArray: ByteArray? = null
    try {
        val out = ObjectOutputStream(bos)
        out.writeObject(this)
        out.flush()
        byteArray = bos.toByteArray()
    }catch (throwable: Throwable) {
        // Do nothing
    }finally {
        try {
            bos.close()
        }catch (throwable: Throwable) {
            // Do nothing
        }
    }

    return byteArray

    /*
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
ObjectOutputStream out = null;
try {
  out = new ObjectOutputStream(bos);
  out.writeObject(yourObject);
  out.flush();
  byte[] yourBytes = bos.toByteArray();
  ...
} finally {
  try {
    bos.close();
  } catch (IOException ex) {
    // ignore close exception
  }
}

Create an object from a byte array:

ByteArrayInputStream bis = new ByteArrayInputStream(yourBytes);
ObjectInput in = null;
try {
  in = new ObjectInputStream(bis);
  Object o = in.readObject();
  ...
} finally {
  try {
    if (in != null) {
      in.close();
    }
  } catch (IOException ex) {
    // ignore close exception
  }
}

     */
}

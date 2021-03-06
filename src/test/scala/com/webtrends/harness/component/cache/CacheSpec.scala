/*
 * Copyright 2015 Webtrends (http://www.webtrends.com)
 *
 * See the LICENCE.txt file distributed with this work for additional
 * information regarding copyright ownership.
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
package com.webtrends.harness.component.cache

import com.webtrends.harness.component.cache.BaseSpecCache.ns

case class SimpleData(a:Int = 0, b:String = "", c:Double = 0.0) extends Cacheable[SimpleData] {
  override def namespace = ns
}

case class SerialData(a:Int = 0, b:String = "", c:Double = 0.0) extends Cacheable[SimpleData] with Serializable {
  override def namespace = ns

  override protected def getBytes: Array[Byte] = serialToBytes(this)

  override protected def extract(obj: Array[Byte])(implicit m: Manifest[SimpleData]): Option[SimpleData] = {
    bytesToSerial(obj)
  }
}

class CacheSpec extends BaseSpecCache {
  "A cacheable object" should {

    "be cacheable" in {
      val obj = SimpleData(1, "two", 3.0)
      val key = new CacheKey(1,"two", false)
      obj.writeInCache(cacheRef, Some(key))

      val found = SimpleData().readFromCache(cacheRef, Some(key))
      found must beEqualTo(Some(SimpleData(1, "two", 3.0))).await()
    }

    "be cacheable" in {
      val obj = SerialData(4, "five", 6.0)
      val key = new CacheKey(4, "five", false)
      obj.writeInCache(cacheRef, Some(key))

      val found = SerialData().readFromCache(cacheRef, Some(key))
      found must beEqualTo(Some(SerialData(4, "five", 6.0))).await()
    }
  }
}

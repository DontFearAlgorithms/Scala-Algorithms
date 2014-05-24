/**
 * @see http://algs4.cs.princeton.edu/53substring/RabinKarp.java.html
 * MonteCarlo only, not Las Vegas
 */
package org.gs.search

import scala.util.Random
import scala.annotation.tailrec

/**
 * @author Gary Struthers
 *
 */
class RabinKarp(pat: String) {
  val R = 256
  val M = pat.length
  val Q = BigInt.probablePrime(31, new Random()).longValue
  val RM = precomputeRM(1, 1)
  val patHash = hash(pat, M)

  @tailrec
  private def precomputeRM(i: Int, rm: Long): Long = {
    if (i == M) rm else precomputeRM(i + 1, (R * rm) % Q)
  }

  private def hash(key: String, M: Int): Long = {
    @tailrec
    def loop(h: Long, j: Int): Long = {
      if (j < M) loop((R * h + key.charAt(j)) % Q, j + 1) else h
    }

    loop(0, 0)
  }

  def search(txt: String): Int = {
    require(txt.length >= M, s"text length:${txt.length} shorter than pattern length:$M")
    val N = txt.length
    var txtHash = hash(txt, M)
    if(patHash == txtHash) 0 else {
      @tailrec
      def loop(i: Int): Int = {
        if(i < N) {
          txtHash = (txtHash + Q - RM * txt.charAt(i - M) % Q) % Q
          txtHash = (txtHash * R + txt.charAt(i)) % Q
          val offset = i - M + 1
          if(patHash == txtHash) offset else loop(i + 1)
        } else N
      }
      
      loop(M)
    }
  }
}


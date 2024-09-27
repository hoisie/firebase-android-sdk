/*
 * Copyright 2023 Google LLC
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

package com.google.firebase.vertexai.type

import java.util.Calendar

/** A response generated by the model. */
public class Candidate
internal constructor(
  public val content: Content,
  public val safetyRatings: List<SafetyRating>,
  public val citationMetadata: CitationMetadata?,
  public val finishReason: FinishReason?
)

/** Safety rating corresponding to a generated content. */
public class SafetyRating
internal constructor(
  public val category: HarmCategory,
  public val probability: HarmProbability,
  public val probabilityScore: Float = 0f,
  public val blocked: Boolean? = null,
  public val severity: HarmSeverity? = null,
  public val severityScore: Float? = null
)

/**
 * A collection of source attributions for a piece of content.
 *
 * @property citations A list of individual cited sources and the parts of the content to which they
 * apply.
 */
public class CitationMetadata internal constructor(public val citations: List<Citation>)

/**
 * Provides citation information for sourcing of content provided by the model between a given
 * [startIndex] and [endIndex].
 *
 * @property title Title of the attribution.
 * @property startIndex The inclusive beginning of a sequence in a model response that derives from
 * a cited source.
 * @property endIndex The exclusive end of a sequence in a model response that derives from a cited
 * source.
 * @property uri A link to the cited source, if available.
 * @property license The license the cited source work is distributed under, if specified.
 * @property publicationDate Publication date of the attribution, if available.
 */
public class Citation
internal constructor(
  public val title: String? = null,
  public val startIndex: Int = 0,
  public val endIndex: Int,
  public val uri: String? = null,
  public val license: String? = null,
  public val publicationDate: Calendar? = null
)

/** The reason for content finishing. */
public enum class FinishReason {
  /** A new and not yet supported value. */
  UNKNOWN,

  /** Model finished successfully and stopped. */
  STOP,

  /** Model hit the token limit. */
  MAX_TOKENS,

  /** [SafetySetting] prevented the model from outputting content. */
  SAFETY,

  /** Model began looping. */
  RECITATION,

  /** Model stopped for another reason. */
  OTHER
}

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

/**
 * A response from the model.
 *
 * @property candidates The list of [Candidate] responses generated by the model.
 * @property promptFeedback Feedback about the prompt send to the model to generate this response.
 * When streaming, it's only populated in the first response.
 * @property usageMetadata Information about the number of tokens in the prompt and in the response.
 */
public class GenerateContentResponse(
  public val candidates: List<Candidate>,
  public val promptFeedback: PromptFeedback?,
  public val usageMetadata: UsageMetadata?,
) {
  /**
   * Convenience field representing all the text parts in the response as a single string, if they
   * exists.
   */
  public val text: String? by lazy {
    candidates.first().content.parts.filterIsInstance<TextPart>().joinToString(" ") { it.text }
  }

  /** Convenience field to list all the [FunctionCallPart]s in the response, if they exist. */
  public val functionCalls: List<FunctionCallPart> by lazy {
    candidates.first().content.parts.filterIsInstance<FunctionCallPart>()
  }
}

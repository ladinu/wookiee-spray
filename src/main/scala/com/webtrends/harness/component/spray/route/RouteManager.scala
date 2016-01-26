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

package com.webtrends.harness.component.spray.route

import com.typesafe.config.Config
import com.webtrends.harness.authentication.CIDRRules
import com.webtrends.harness.component.spray.directive.CIDRDirectives
import spray.routing._

import scala.collection.mutable
import scala.collection.mutable.{HashMap, SynchronizedMap}

/**
 * @author Michael Cuthbert on 11/14/14.
 */
object RouteManager extends CIDRDirectives {
  var cidrRules:Option[CIDRRules] = None
  def apply(config:Config) = cidrRules = Some(CIDRRules(config))

  private val routes = new HashMap[String, Route] with SynchronizedMap[String, Route]

  def addRoute(name: String, route: Route) = {
    externalLogger.debug(s"new route registered with route manager [$name]")
    routes += name -> route
  }

  def removeRoute(name: String, route: Route) = {
    externalLogger.debug(s"route unregistered with route manager [$name]")
    routes.remove(name)
  }

  def getRoute(service: String): Option[Route] = routes.get(service)

  def getRoutes(routeFilter: (String) => Boolean): Iterable[Route] = routes.filterKeys(routeFilter).values
}

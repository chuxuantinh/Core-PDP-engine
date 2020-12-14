# Change log
All notable changes to this project are documented in this file following the [Keep a CHANGELOG](http://keepachangelog.com) conventions. 

Issues reported on [GitHub](https://github.com/authzforce/core/issues) are referenced in the form of `[GH-N]`, where N is the issue number. Issues reported on [OW2](https://jira.ow2.org/browse/AUTHZFORCE/) are mentioned in the form of `[OW2-N]`, where N is the issue number.


## Unreleased
### Changed
- Parent project version: 6.0.0 -> 7.0.0:
	- Changed managed Spring version: 4.3.6 -> 4.3.12
- Dependency version: core-pdp-api: 11.0.0 ->12.0.0
- Changed PDP configuration XSD: 5.0.0 -> 6.0.0:
	- Replaced attribute `badRequestStatusDetailLevel` with `clientRequestErrorVerbosityLevel`
	- Replaced attributes `requestFilter` and `resultFilter` with element `ioProcChain` of new type `InOutProcChain` defining a pair of request preprocessor (ex-requestFilter) and result postprocessor (ex-resultFilter)
	- Added `maxIntegerValue` attribute allowing to define the expected max integer value to be handled by the PDP engine during evaluation, based on which the engine selects the best Java representation among several (BigInteger, Long, Integer) for memory and CPU usage optimization
- Renamed PDP engine interfaces and base implementations:
	* `(Base|Closeable)AttributeProviderModule` -> `(Base|Closeable)DesignatedAttributeProvider`
	* `(Base)RequestFilter` -> `(Base)DecisionRequestPreprocessor`
	* `DecisionResultFilter` -> `DecisionResultPostprocessor`
	* `CloseablePdp` -> `CloseablePdpEngine`
	* `(Base|Closeable)(Static)RefPolicyProviderModule` -> `(Base|Closeable)(Static)RefPolicyProvider`
	* `RootPolicyProviderModule` -> `RootPolicyProvider`
	* `(Base)DatatypeFactory(Registry)` -> `(Base)AttributeValueFactory(Registry)` (using new `AttributeDatatype` subclass of `Datatype`)
- Core PDP engine made agnostic of decision request/response formats, and extensible through `PdpEngineInoutAdapter` interface, and more specifically `DecisionRequestPreprocessor` and `DecisionResultPostprocessor` interfaces, in order to support new types of input/output (SerDes) formats (native implementations provided for XACML 3.0/XML - core specification - using JAXB API, and XACML/JSON - JSON Profile of XACML 3.0)
- Identifiers of native PDP requestFilter/resultFilter (now requestPreproc/resultPostproc) extensions: 
	- *...:request-filter:...* renamed to *...:request-preproc:xacml-xml:...*
	- *...result-filter:...* renamed to *...:result-postproc:xacml-xml:...*	
- Replaced `JaxbXacmlUtils` utility class with `Xacml3JaxbHelper` (in authzforce-ce-xacml-model dependency)
- Changed naming convention for Java class names with acronym(s) (only first letter should be uppercase), e.g. PolicyPOJO -> PolicyPojo

### Added	
- Module `pdp-io-xacml-json` - XACML JSON Profile implementation: provides PDP extensions for processing (request/result pre/postprocessors) JSON input/output formats defined by JSON Profile of
XACML 3.0, and adapting to the PDP engine API; also provides automatic conversion of OASIS XACML 3.0/XML conformance test to XACML/JSON format (JSON Profile of XACML 3.0) with XSLT.
- Module `pdp-cli`: provides a PDP command-line interface and produces an executable jar allowing to test the PDP engine on the command line
- PDP engine I/O adapter extension mechanism for supporting new input/output formats of decision requests/responses
- `PdpEngineAdapters` utility class to help instantiate PDP engines supporting specific input/output formats
- `PpEngineConfiguration` utility class to help instantiate a PDP engine from a PDP XML configuration file (valid against PDP configuration XSD)


## 9.1.0
### Changed 
- MongoDBRefPolicyProviderModule class: removed useless method already implemented by super class BaseStaticRefPolicyProviderModule.


## 9.0.1
### Fixed
- Latest versions in Changelog 


## 9.0.0
### Changed
- Version of parent project: 6.0.0:
  - The XML schema definition of PDP Decision Cache extensions' base type have been simplified (a few attributes removed).
- Version of dependency authzforce-ce-core-pdp-api: 11.0.0 (API changes):
  - Changed PDPEngine interface methods
  - Changed PDP extensions' interface methods: DecisionResultFilter, RequestFilter, DecisionCache (new EvaluationContext parameter to enable context-dependent caches), RefPolicyProvider (renamed RefPolicyProvider.Utils class to RefPolicyProvider.Helper).
  - Changed EvaluationContext interface methods: 
  		- Use of Bag replaced with AttributeBag class (AttributeBags are Bags with extra metadata such as the source - AttributeSource - of the attribute values: request, PDP, attribute provider extension, etc.
  		- New methods to help PDP extensions to watch for changes to the context with listeners
  - Changed Expression interface methods
  - Changed VersionPatterns class methods to return new PolicyVersionPattern class that helps manipulate XACML VersionMatchTypes
  - Renamed class IndividualDecisionRequest to IndividualXACMLRequest (XACML-specific model of Individual Decision Request)
  - Renamed class IndividualPdpDecisionRequest to PdpDecisionRequest (individual request in XACML-agnostic AuthzForce model)
  - Renamed class AttributeGUID(s) to AttributeFQN(s) (Fully Qualified Name is more appropriate than GUID)
  - Renamed class MutableBag to MutableAttributeBag
  - Aded BaseStaticRefPolicyProviderModule class as convenient base class for implementing static Policy Provider (StaticRefPolicyProviderModule) implementations

### Added
- [PolicyProvider implementation](pdp-testutils/src/main/java/org/ow2/authzforce/core/pdp/testutil/ext/MongoDBRefPolicyProviderModule.java) for testing and documentation purposes, using MongoDB as policy database system and Jongo as client library, with [JUnit test class](pdp-testutils/src/test/java/org/ow2/authzforce/core/pdp/testutil/test/MongoDBRefPolicyProviderModuleTest.java) showing how to use it.


## 8.0.0
### Changed
- Version of parent project: 5.1.0
- Version of dependency authzforce-ce-core-pdp-api: 9.1.0 (API changes)
- License: GPL v3.0 replaced with Apache License v2.0
- Project URL: 'https://tuleap.ow2.org/projects/authzforce' replaced with 'https://authzforce.ow2.org'
- GIT repository URL base: 'https://tuleap.ow2.org/plugins/git/authzforce' replaced with 'https://gitlab.ow2.org/authzforce'
- Project converted to multi-module project with two new modules in order to have properly separated artifact with the test utility classes to be reused in other AuthzForce projects (e.g. `server/webapp` and PDP extensions), therefore two new Maven artifacts:
	- `authzforce-ce-core-pdp-engine` replacing artifact `authzforce-ce-core` (no classifier);
	- `authzforce-ce-core-pdp-testutils` replacing artifact `authzforce-ce-core` with `tests` classifier.


## 7.1.0
### Added
- [OW2-26] Simplify evaluation of Apply expression with commutative numeric function f (e.g. add and multiply): if multiple arguments are constants A, B..., then: `f(a1,...an, A, b1,...bn, B, c1,...) = f(C, a1,...an, b1,...bn, c1...)` where `C = f(A,B...)` and a1,...an, b1,...bn, c1,... are the other arguments (variables).

### Fixed
- [OW2-25] - Reopened - NullPointerException when parsing Apply expressions using invalid/unsupported Function ID. This is the final fix addressing higher-order functions. (Initial fix only addressed first-order ones.)
- Artifact `authzforce-ce-core` with `tests` classifier: missing classes.


## 7.0.0
### Changed
- Changed parent version: 4.1.1 -> 5.0.0
- Changed dependency versions:
	- AuthzForce Core PDP API: 8.2.0 -> 9.0.0
	- SLF4J: 1.7.6 -> 1.7.22
	- Spring: 4.3.5 -> 4.3.6
	- Guava: 20.0 -> 21.0
- Renamed `PDPImpl` class to `BasePdpEngine` implements new `PDPEngine` API

### Removed
- Removed/Merged `PdpConfigurationParser` class into new `BasePdpEngine` class (replacing `PDPImpl`)

### Added
- Unit test of CXF authorization interceptor (web service PEP) using AuthForce PDP engine, based on
 @coheiga's [XACML 3.0 Authorization Interceptor test](https://github.com/coheigea/testcases/blob/master/apache/cxf/cxf-sts-xacml/src/test/java/org/apache/coheigea/cxf/sts/xacml/authorization/xacml3/XACML3AuthorizationTest.java)


## 6.1.0
### Changed
- Parent project version: 4.0.0 -> 4.1.1 => Changed dependency versions: 
    - Spring 4.3.4 -> 4.3.5, 
    - Saxon-HE 9.7.0-11 -> 9.7.0-14
- authzforce-ce-core-pdp-api dependency version: 8.0.0 -> 8.2.0

### Fixed
- Security issues reported by Find Security Bugs plugin


## 6.0.0
### Added
- Extension mechanism to switch HashMap/HashSet implementation; default implementation is based on native JRE and Guava.
- Validation of 'n' argument (minimum of *true* arguments) of XACML 'n-of' function if this is constant (must be a positive integer not greater than the number of remaining arguments)
- Validation of second and third arguments of XACML substring function if these are constants (arg1 >= 0 && (arg2 == -1 || arg2 >= arg1))
- Maven plugin owasp-dependency-check to check vulnerabilities in dependencies

### Changed
- Maven parent project version: 3.4.0 -> 4.0.0:
	- [GH-4] **Java version: 1.7 -> 1.8**
	- Guava dependency version: 18.0 -> 20.0
	- Saxon-HE dependency version: 9.6.0-5 -> 9.7.0-11
	- com.sun.mail:javax.mail v1.5.4 changed to com.sun.mail:mailapi v1.5.6
- Dependency authzforce-ce-core-pdp-api 7.1.1 -> 8.0.0
- Behavior of *unordered* rule combining algorithms (deny-overrides, permit-overrides, deny-unless-permit and permit-unless deny), i.e. for which the order of evaluation may be different from the order of declaration: child elements are re-ordered for more efficiency (e.g. Deny rules evaluated first in case of deny-overrides algorithm), therefore the algorithm implementation, the order of evaluation in particular, now differs from ordered-* variants.

### Removed
- Dependency on Koloboke, replaced by extension mechanism mentioned in *Added* section that would allow to switch from the default HashMap/HashSet implementation to Koloboke-based.

### Fixed
- [OW2-23] Enforcement of RuleId/PolicyId/PolicySetId uniqueness:
	- PolicyId (resp. PolicySetId) should be unique across all policies loaded by PDP so that PolicyIdReferences (resp. PolicySetIdReferences) in Responses' PolicyIdentifierList are absolute references to applicable policies (no ambiguity).
 	- [RuleId should be unique within a policy](https://lists.oasis-open.org/archives/xacml/201310/msg00025.html) -> A rule is globally uniquely identified by the parent PolicyId and the RuleId.
- [OW2-25] NullPointerException when parsing Apply expressions using invalid/unsupported Function ID. Partial fix addressing only invalid first-order functions. See release 7.0.1 for final fix addressing higher-order functions too.


## 5.0.2
### Changed
- Dependency version: authzforce-core-pdp-api: 7.1.1 (was: 7.1.0)


## 5.0.1
### Fixed
- [OW2-22] When handling the same XACML Request twice in the same JVM with the root PolicySet using deny-unless-permit algorithm over a Policy returning simple Deny (no status/obligation/advice) and a Policy returning Permit/Deny with obligations/advice, the obligation is duplicated in the final result at the second time this situation occurs. 
- XACML StatusCode XML serialization/marshalling error when Missing Attribute info that is no valid anyURI is returned by PDP in a Indeterminate Result
- Memory management issue: native RootPolicyProvider modules keeping a reference to static refPolicyProvider, even after policies have been resolved statically at initialization time, preventing garbage collection and memory saving.
- Calls to Logger impacted negatively by autoboxing

### Removed
- 'functionSet' element no longer supported in PDP XML configuration schema

### Changed
- PDP XML configuration schema namespace: http://authzforce.github.io/core/xmlns/pdp/5.0 (previous namespace: http://authzforce.github.io/core/xmlns/pdp/3.6). See *Removed* section for non-backward-compatible changes to the schema.
- Parent project version: authzforce-ce-parent: 3.4.0
- Dependency version: authzforce-ce-core-pdp-api: 7.1.0: requires to pass new EnvironmentProperties parameter to AttributeProvider module factories for using global PDP environment properties (such as PDP configuration file's parent directory)
- Interpretation of XACML Request flag ReturnPolicyId=true, considering a policy "applicable" if and only if the decision is not NotApplicable and if it is not a root policy, the same goes for the enclosing policy. See also the discussion on the xacml-comment mailing list: https://lists.oasis-open.org/archives/xacml-comment/201605/msg00004.html

### Added
- New PDP configuration parameter: 'standardEnvAttributeSource' (enum) sets the source for the Standard Current Date/Time Environment Attribute values (current-date, current-time, current-dateTime), possible values: PDP_ONLY, REQUEST_ELSE_PDP, REQUEST_ONLY
- New PDP configuration parameter: 'badRequestStatusDetailLevel': Level of detail in the StatusDetail returned in Indeterminate Results when the XACML Request syntax/content is invalid. Increasing this value usually helps better pinpoint the issue with the Request.
- enum StandardFunction that enumerates all standard XACML function IDs
- enum StandardEnvironmentAttribute that enumerates all XACML standard environment attribute identifiers
- enum StandardCombiningAlgoritm that enumerates all standard XACML combining algorithms

### Deprecated
- Ability to marshall internal classes derived from XACML/JAXB Expressions back to the original JAXB Expression: it may consume a significant amount of extra memory, esp. when a nested PolicySet has deep nested Policy(Set)s, and it forces our internal evaluation classes to duplicate information and override many methods. Also it ties the internal model to the JAXB model which is far from optimal for evaluation purposes. Now we consider no longer the responsibility of the PDP to be able to marshall such XACML instances, but the caller's; in particular the classes ApplyExpression, AttributeDesignatorExpression, AttributeSelectorExpression, AttributeAssigmnentExpressionEvaluator no longer extend JAXB classes.


## 4.0.2
### Fixed
- Issues reported by Codacy (including fixed issues in upgraded dependency core-pdp-api 4.0.2)


## 4.0.0
### Changed
- Native PDP request filter IDs (values of `pdp` configuration element's `requestFilter` attribute):
	- `urn:ow2:authzforce:xacml:request-filter:default-lax` changed to `urn:ow2:authzforce:feature:pdp:request-filter:default-lax`;
	- `urn:ow2:authzforce:xacml:request-filter:default-strict` changed to `urn:ow2:authzforce:feature:pdp:request-filter:default-strict`;
	- `urn:ow2:authzforce:xacml:request-filter:multiple:repeated-attribute-categories-strict` changed to `urn:ow2:authzforce:feature:pdp:request-filter:multiple:repeated-attribute-categories-strict`;
	- `urn:ow2:authzforce:xacml:request-filter:multiple:repeated-attribute-categories-lax` changed to `urn:ow2:authzforce:feature:pdp:request-filter:multiple:repeated-attribute-categories-lax`.

### Fixed
- Maven dependency: authzforce-ce-core-pdp-api upgraded to v4.0.0 fixing license headers 
- Fixed license headers (current year)
- Fixed out-of-date documentation in pdp.xsd on PDP extensions


## 3.9.0 
### Added
- New PdpExtensionLoader method providing the list of available extensions of a given type: datatype, function, combining algorithm, etc.

### Removed
- dnsName-equal and ipAddress-equal functions, which are not to be used because they are not in XACML spec (regexp-match equivalent must be used instead)

### Fixed
- NullPointerException when defining unknown combining algorithm ID in PDP configuration
- PdpExtensionLoader throwing IllegalArgumentException if no extension found of this type, instead of returning an empty list when the extension type is actually valid but no extension found

### Tests
- New tests for custom extensions: result filter (implements CombinedDecision from XACML Multiple Decision Profile), simple datatype (dnsname-value from XACML DLP/NAC Profile), complex datatype (XACML Policy), function (dnsname-value-equal from XACML DLP/NAC Profile), combining algorithm (on-permit-apply-second from XACML Additional Combining Algorithms Profile)


## 3.8.3
### Fixed 
- Removing Javadoc @author tag added automatically by maven Javadoc plugin without us knowing
- PDP schema: removed limits (100) for maxVarRefDepth and maxPolicyRefDepth attributes. Hard arbitrary limits should not be in the XML schema.


## 3.8.2
### Fixed
- Javadoc comments


## 3.8.1
### Fixed
- Removed use of SAXON StandardURIChecker for validating anyURI XACML AttributeValues causing "possible memory leak" errors in Tomcat, as confirmed by: https://sourceforge.net/p/saxon/mailman/message/27043134 and https://sourceforge.net/p/saxon/mailman/saxon-help/thread/4F9E683E.8060001@saxonica.com/. Although XACML 3.0 still refers to XSD 1.0 which has a stricter definition of anyURI than XSD 1.1, the fix consisted to use XSD 1.1 anyURI definition for XACML anyURI AttributeValues. In this definition, anyURI and string datatypes have same value space (refer to XSD 1.1 Datatypes document or SAXON note http://www.saxonica.com/html/documentation9.4/changes/intro93/xsd11-93.html or mailing list: https://sourceforge.net/p/saxon/mailman/saxon-help/thread/4F9E683E.8060001@saxonica.com/) , therefore anyURI-specific validation is removed and anyURI values are accepted like string values by the program. However, this does not affect XML schema validation of Policy/PolicySet/Request documents against OASIS XACML 3.0 schema, where the XSD 1.0 definition of anyURI still applies.


## 3.8.0
### Changed
- PDP XML schema: maxVariableRefDepth and maxPolicyRefDepth attributes made optional (instead of required)

### Added
- PDP XML schema: 'requestFilter' attribute (RequestFilter extension): 
	- Added documentation about natively supported values, with '-lax' suffix meaning that duplicate <Attribute> with same meta-data in the same <Attributes> element of a Request is allowed (in compliance with XACML 3.0 core spec, §7.3.3), and '-strict' suffix meaning that it is not allowed (not strictly compliant with XACML 3.0 Core, section 7.3.3):
		- 'urn:ow2:authzforce:xacml:request-filter:default-lax' and 'urn:ow2:authzforce:xacml:request-filter:default-strict': default requestFilter limited to what is specified in XACML 3.0 Core specification
		- 'urn:ow2:authzforce:xacml:request-filter:multiple:repeated-attribute-categories-lax' and 'urn:ow2:authzforce:xacml:request-filter:multiple:repeated-attribute-categories-strict': implement Multiple Decision Profile, section 2.3 (repeated attribute categories)
	- Added XSD-defined default value for this 'requestFilter' attribute: 'urn:ow2:authzforce:xacml:request-filter:default-lax'
- Support for Extended Indeterminate values (XACML 3.0 Core specification, section 7.10-7.14, appendix C: combining algorithms)
- PdpImpl#getStaticApplicablePolicies() method that provides all the PDP's applicable policies (root and referenced - directly or indirectly - from the root policy) if all are statically resolved. This allows PDP clients to know all the policies (if statically resolved) possibly used by the PDP during the evaluation.


## 3.7.0
### Added
- Root policy provider module based on any policy-by-reference provider (parameter is the root policy reference to be resolved by the policy-by-reference provider)

### Changed
- PDP configuration XSD version -> 3.6.1 (supporting new configuration type for the new ref-based Root policy provider module mentioned in previous section)

### Removed
- Moved/Refactored API classes sufficient for implementing PDP extensions (Datatypes, Functions, Policy/Attribute providers, etc.) to a separate project: authzforce-ce-core-pdp-api

### Fixed
- Broken validation of max policy reference depth


## 3.6.0
### Added
- Support all [XACML 3.0 conformance tests](https://lists.oasis-open.org/archives/xacml-comment/201404/msg00001.html) published by AT&T on XACML mailing list in March 2014, except IIA010, IIA012, IIA024, IID029, IID030, III.C.2, III.C.3, IIIE301, IIIE303, II.G.2-6 (see also [README](pdp-testutils/src/test/resources/conformance/xacml-3.0-from-2.0-ct/README.md) ); with specific adaptations and enhancements:
  1. XACML 3.0 Schema validation in all conformance tests (original files are not all compliant with XACML 3.0). 
  1. The original conformance test folder contains hundreds of files; for better readability and management, the folder is split in *mandatory* folder for tests on supported mandatory features (XACMl 3.0 core), *optional* folder for supported optional features (XACML 3.0 core and profiles), and *unsupported* for unsupported features.
  1. For tests requiring a custom attribute finder, added a file with suffix `AttributeProvider.xml` that configures the `TestAttributeProviderModule`. This configuration file must contain a list of `Attributes` elements defining the attributes that this attribute provider is able to provide, with their constant values.
  1. For tests requiring policies to be referenced via Policy(Set)IdReferences, added a directory named `refPolicies` containing a XACML Policy(Set) file per referenced Policy(Set).
  1. For tests of Request syntax validation (syntax error expected to be detected by Authzforce PDP at initialization-time, i.e. before any Request evaluation), added suffix `.ignore` to the original test Policy(Set) and Response files.
  1. For tests of Policy(Set) syntax validation (syntax error expected to be detected by Authzforce PDP at initialization-time, i.e. before any Request evaluation), added suffix `.ignore` to the original test Request and Response files.
- [HTML description](pdp-testutils/src/test/resources/conformance/xacml-3.0-from-2.0-ct/ConformanceTests.html) of XACML 3.0 conformance tests
- Support of Policy(Set)Version in Policy(Set)IdReference handled by the native policy finder
- Support for Variable evaluation in Policy with scope management (variable is local to Policy where defined and inherited by Rules)
- Added support of xpathExpressions (optional XACML feature) in Request with support of namespace-prefix mappings extracted from XML document (XACML Request/Policy(Set)/Rule) (typically via `xmlns` declarations) where the xpathExpression is defined, e.g. XACML Request or Policy(Set).
- PDP configuration option to enable/disable XPath support (evaluation of xpathExpression datatype in Request/Policy(Set)/Rule, AttributeSelector and xpath functions)
- Added support of RequestDefaults/XPathVersion (optional XACML features) for evaluation of xpathExpressions in Request, and PolicyDefaults/XPathVersion (optional XACML feature) for evaluation of xpathExpressions and AttributeSelectors in Policy(Set) documents.
- Added support of ReturnPolicyIdList (optional XACML feature) to return identifiers of policies found applicable for the Request
- Added support of xpath-node-count function (optional XACML feature)
- New modes of request parsing/filtering and attribute matching to enforce best practices and optimize Request processing:
  1. *Strict Attribute Issuer match*: in this mode, an AttributeDesignator without Issuer only matches XACML Request Attributes without Issuer (faster if all Attributes have an Issuer which is recommended, but not fully XACML (§5.29) compliant)
  2. *Allow Attribute duplicates*: allows defining multi-valued attributes by repeating the same XACML Attribute (same AttributeId) within a XACML Attributes element (same Category). Indeed, not allowing this enables the PDP to parse and evaluate Requests more efficiently, especially if you know the Requests to be well-formed, i.e. all AttributeValues of a given Attribute are grouped together in the same `<Attribute>` element. However, it may not be fully compliant with the XACML spec according to a [discussion](https://lists.oasis-open.org/archives/xacml-dev/201507/msg00001.html) on the xacml-dev mailing list, referring to the XACML 3.0 core spec, §7.3.3, that indicates that multiple occurrences of the same `<Attribute>` with same meta-data but different values should be considered equivalent to a single `<Attribute>` element with same meta-data and merged values (multi-valued Attribute). Moreover, the XACML 3.0 conformance test 'IIIA024' expects this behavior: the multiple subject-id Attributes are expected to result in a multi-value bag during evaluation of the `<AttributeDesignator>`.
- Features to prevent circular references in Policy(Set)IdReferences or VariableReference
- Features to limit depth of PolicySetIdReference or VariableReference chain (otherwise no theoretical limit)

### Changed
- TestMatchAlg class replaced with official conformance test on Target matching: group II.B.
- Improved `TestUtils` class to allow configuring a directory of referenced policies for Policy(Set)IdReferences, to enable/disable XPath support, and to configure a specific RequestFilter ID, e.g. to use the MultipleDecisionProfile for conformance tests of 'optional' features.
- Renamed RELEASE-NOTES.md to CHANGELOG.md to adopt conventions from [keepachangelog.com](http://keepachangelog.com).
- Logback dependency scope (maven) from `compile` to `test` (not required for compiling, only for tests, any SLF4J-compatible library may be used at runtime).
- Moved old README.md content to the server project since it does not apply anymore to this project but to the AuthzForce server project.

### Fixed 
- Issues reported by PMD and findbugs
- Fixed issues in [XACML 3.0 conformance tests](https://lists.oasis-open.org/archives/xacml-comment/201404/msg00001.html) published by AT&T on XACML mailing list in March 2014, see [README](pdp-testutils/src/test/resources/conformance/xacml-3.0-from-2.0-ct\README.md).
- In logical OR, AND and N-OF functions, an Indeterminate argument results in Indeterminate result. 
  1. FIX for OR function: If at least one True argument, return True regardless of Indeterminate arguments; else (no True) if there is at least one Indeterminate, return Indeterminate, return Indeterminate; else (no True/Indeterminate -> all false) return false
  1. FIX for AND function: If at least one False argument, return False regardless of Indeterminate arguments; else (no False) if there is at least one Indeterminate, return Indeterminate, return Indeterminate; else (no False/Indeterminate -> all true) return true
  1. FIX for N-OF function: similar to OR but checking if there are at least N Trues instead of 1, in the remaining arguments; else there is/are n True(s) with `n < N`; if there are at least `(N-n)` Indeterminate, return Indeterminate; else return false.
- Misleading IllegalArgumentException error for XML-schema-valid anyURI but not valid for `java.net.URI` class. Fixed by using `java.lang.String` instead and validating strings according to anyURI definition with Saxon library
- RuntimeException when no subject and no resource and no action attributes in the XACML request


## 3.5.8 - 2015-04-01
### Added
- New XACML 3.0 versions of (ordered-)deny-overrides and (ordered-)permit-overrides combining algorithms (ALGORITHM IS NOT THE SAME as in XACML 2.0)

### Changed
- Renamed classes of XACML 1.0/2.0 combining algorithms (Ordered)DenyOverrides and (Ordered)PermitOverrides to Legacy*, and replaced with new XACML 3.0 versions

### Fixed
- Empty StatusDetail tag in Response when no StatusDetail (which is always the case as of now). Fix: remove the tag completely.


## 3.5.7 - 2015-03-13
### Changed
- Upraded version of maven-jaxb2-plugin to 0.12.3 for JAXB-annotated java class generation from OASIS XACML model


## 3.5.6 - 2015-02-27
### Added
- Generic test class for non-regression tests
- TestsAttributeFinder class for tests with a mock attribute finder (e.g. in non-regression tests)
- Functional unit test for Multiple Decision Profile with repeated categories (section 2.3 of XACML MDP)

### Changed
- Changed PDP *evaluate* method return type to standard XACML Response (JAXB-annotated)
- More explicit error messages for illegal parameters to functions: function ID, expected argument type, number of arguments, etc.

### Fixed
- NullPointerException with Indeterminate result of evaluating XACML AllOf or if no AllOf matches in a AnyOf
- NullPointerException when no resource-id attribute in XACML Request: 
- XACML Apply element marshalling (some elements were lost)


## 3.5.5 - 2015-01-26
### Added
- PDP configuration XML schema for configuration loading with JAXB and schema validation
- Framework for plugging PDP extensions (attribute/policy finders) by configuration, without re-compiling
- PDP Bean class usable as JNDI resource

### Changed
- License changed to GPLV3
- Upgrade code to use new Java 7 features
- Policy finder change: FilePolicyModule replaced with StaticPolicyFinderModule that supports loading policy files from any Spring-compatible resource URL

### Fixed
- Thread-local memory leak
- Empty Obligations/Associated Advice with permit|deny-unless-deny|permit combining algorithms


## 3.5.4 - 2014-12-23
### Added
- Unit tests for various match functions introduced in XACML 2.0 on strings, x509Names, rfc822Names, date/time, IP address
- Unit tests on date/time arithmetic functions, number arithmetic functions
- Unit tests for Set functions, higher-order bag functions
- Unit tests for logical functions
- Implementations of date/time artithemtic functions, number arithmetic, string-equal-*, higher-order bag functions
- Unit tests for "abstract" functions, e.g. 'map'
- Logback dependency for logging

### Changed
- Log formats


## 3.5.3 - 2013-12-16
### Added
- Support of Policy(Set)IdReference with StaticRefPolicyFinder class
- Support of dynamic obligations/advices containing AttributeDesignators or other expressions evaluated in the request context
- Enhanced debug logs in evaluation of Target, Policy(Set), Rule


## 3.5.2 - 2013-11-29
### Fixed
- Fixed bug when there were more than one AnyOf and AllOf: only the Match element was evaluated with the "match(context)" function


## 3.4.2 - 2013-07-03
### Fixed
- Fixing bugs on deny-unless-permit and permit-unless-deny rule combining algorithms (misplaced cast)


## 3.4.0 - 2013-05-30
### Added
- Implementation working with XACML 3.0 requests and policies compliant with OASIS XACML model (xsd)
- Partial implementation of the Multiple Decision Profile. The MultiRequests scheme is not implemented yet
- Functionnal tests added for XACML 3.0 model. This is actually the OASIS functional tests translated to a v3.O model.
- Implementation of the "IncludeInResult" attribute
- Support of XACML Obligations
- Support of XACML Advices
- Apache 2.0 licence headers added to every source file
- First implementation of XACML 3.0 Combining algorithms: deny-unless-permit, deny-unless-permit, permit-unless-deny, permit-unless-deny
- First implementation of XACML 3.0 Functions: string-starts-with, string-ends-with, string-contains, string-substring


## 3.3.1 - 2013-05-14
### Added
- New license headers and file for Apache 2 license


## 3.2.0 - 2013-05-13
### Added
- Support of XACML 3.0 Obligations/Advices in Rules
- Compliance with new conformance tests for 3.0 (converted from XACML 2.0 official category III.A)


## 3.1.0 - 2013-05-13
### Added
- Beta support of Multiple Decision profile, on repeated attribute categories only
- Beta support of XACML 3.0 Policy(Set)s and Obligations/Advices in Policy(Set)s


## 3.0.0 - 2013-04-05
### Added
- Preliminary support of XACML 3.0


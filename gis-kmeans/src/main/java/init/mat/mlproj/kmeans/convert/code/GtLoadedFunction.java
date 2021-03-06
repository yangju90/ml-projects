package init.mat.mlproj.kmeans.convert.code;

public class GtLoadedFunction {

    public String getCore() {
        return core;
    }

    public String getMain() {
        return main;
    }

    private String core = "(function (global, factory) {\n" +
            "\ttypeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :\n" +
            "\ttypeof define === 'function' && define.amd ? define(['exports'], factory) :\n" +
            "\t(factory((global.topojson = global.topojson || {})));\n" +
            "}(this, (function (exports) { 'use strict';\n" +
            "\n" +
            "var identity = function(x) {\n" +
            "  return x;\n" +
            "};\n" +
            "\n" +
            "var transform = function(transform) {\n" +
            "  if (transform == null) return identity;\n" +
            "  var x0,\n" +
            "      y0,\n" +
            "      kx = transform.scale[0],\n" +
            "      ky = transform.scale[1],\n" +
            "      dx = transform.translate[0],\n" +
            "      dy = transform.translate[1];\n" +
            "  return function(input, i) {\n" +
            "    if (!i) x0 = y0 = 0;\n" +
            "    var j = 2, n = input.length, output = new Array(n);\n" +
            "    output[0] = (x0 += input[0]) * kx + dx;\n" +
            "    output[1] = (y0 += input[1]) * ky + dy;\n" +
            "    while (j < n) output[j] = input[j], ++j;\n" +
            "    return output;\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "var bbox = function(topology) {\n" +
            "  var t = transform(topology.transform), key,\n" +
            "      x0 = Infinity, y0 = x0, x1 = -x0, y1 = -x0;\n" +
            "\n" +
            "  function bboxPoint(p) {\n" +
            "    p = t(p);\n" +
            "    if (p[0] < x0) x0 = p[0];\n" +
            "    if (p[0] > x1) x1 = p[0];\n" +
            "    if (p[1] < y0) y0 = p[1];\n" +
            "    if (p[1] > y1) y1 = p[1];\n" +
            "  }\n" +
            "\n" +
            "  function bboxGeometry(o) {\n" +
            "    switch (o.type) {\n" +
            "      case \"GeometryCollection\": o.geometries.forEach(bboxGeometry); break;\n" +
            "      case \"Point\": bboxPoint(o.coordinates); break;\n" +
            "      case \"MultiPoint\": o.coordinates.forEach(bboxPoint); break;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  topology.arcs.forEach(function(arc) {\n" +
            "    var i = -1, n = arc.length, p;\n" +
            "    while (++i < n) {\n" +
            "      p = t(arc[i], i);\n" +
            "      if (p[0] < x0) x0 = p[0];\n" +
            "      if (p[0] > x1) x1 = p[0];\n" +
            "      if (p[1] < y0) y0 = p[1];\n" +
            "      if (p[1] > y1) y1 = p[1];\n" +
            "    }\n" +
            "  });\n" +
            "\n" +
            "  for (key in topology.objects) {\n" +
            "    bboxGeometry(topology.objects[key]);\n" +
            "  }\n" +
            "\n" +
            "  return [x0, y0, x1, y1];\n" +
            "};\n" +
            "\n" +
            "var reverse = function(array, n) {\n" +
            "  var t, j = array.length, i = j - n;\n" +
            "  while (i < --j) t = array[i], array[i++] = array[j], array[j] = t;\n" +
            "};\n" +
            "\n" +
            "var feature = function(topology, o) {\n" +
            "  return o.type === \"GeometryCollection\"\n" +
            "      ? {type: \"FeatureCollection\", features: o.geometries.map(function(o) { return feature$1(topology, o); })}\n" +
            "      : feature$1(topology, o);\n" +
            "};\n" +
            "\n" +
            "function feature$1(topology, o) {\n" +
            "  var id = o.id,\n" +
            "      bbox = o.bbox,\n" +
            "      properties = o.properties == null ? {} : o.properties,\n" +
            "      geometry = object(topology, o);\n" +
            "  return id == null && bbox == null ? {type: \"Feature\", properties: properties, geometry: geometry}\n" +
            "      : bbox == null ? {type: \"Feature\", id: id, properties: properties, geometry: geometry}\n" +
            "      : {type: \"Feature\", id: id, bbox: bbox, properties: properties, geometry: geometry};\n" +
            "}\n" +
            "\n" +
            "function object(topology, o) {\n" +
            "  var transformPoint = transform(topology.transform),\n" +
            "      arcs = topology.arcs;\n" +
            "\n" +
            "  function arc(i, points) {\n" +
            "    if (points.length) points.pop();\n" +
            "    for (var a = arcs[i < 0 ? ~i : i], k = 0, n = a.length; k < n; ++k) {\n" +
            "      points.push(transformPoint(a[k], k));\n" +
            "    }\n" +
            "    if (i < 0) reverse(points, n);\n" +
            "  }\n" +
            "\n" +
            "  function point(p) {\n" +
            "    return transformPoint(p);\n" +
            "  }\n" +
            "\n" +
            "  function line(arcs) {\n" +
            "    var points = [];\n" +
            "    for (var i = 0, n = arcs.length; i < n; ++i) arc(arcs[i], points);\n" +
            "    if (points.length < 2) points.push(points[0]); // This should never happen per the specification.\n" +
            "    return points;\n" +
            "  }\n" +
            "\n" +
            "  function ring(arcs) {\n" +
            "    var points = line(arcs);\n" +
            "    while (points.length < 4) points.push(points[0]); // This may happen if an arc has only two points.\n" +
            "    return points;\n" +
            "  }\n" +
            "\n" +
            "  function polygon(arcs) {\n" +
            "    return arcs.map(ring);\n" +
            "  }\n" +
            "\n" +
            "  function geometry(o) {\n" +
            "    var type = o.type, coordinates;\n" +
            "    switch (type) {\n" +
            "      case \"GeometryCollection\": return {type: type, geometries: o.geometries.map(geometry)};\n" +
            "      case \"Point\": coordinates = point(o.coordinates); break;\n" +
            "      case \"MultiPoint\": coordinates = o.coordinates.map(point); break;\n" +
            "      case \"LineString\": coordinates = line(o.arcs); break;\n" +
            "      case \"MultiLineString\": coordinates = o.arcs.map(line); break;\n" +
            "      case \"Polygon\": coordinates = polygon(o.arcs); break;\n" +
            "      case \"MultiPolygon\": coordinates = o.arcs.map(polygon); break;\n" +
            "      default: return null;\n" +
            "    }\n" +
            "    return {type: type, coordinates: coordinates};\n" +
            "  }\n" +
            "\n" +
            "  return geometry(o);\n" +
            "}\n" +
            "\n" +
            "var stitch = function(topology, arcs) {\n" +
            "  var stitchedArcs = {},\n" +
            "      fragmentByStart = {},\n" +
            "      fragmentByEnd = {},\n" +
            "      fragments = [],\n" +
            "      emptyIndex = -1;\n" +
            "\n" +
            "  // Stitch empty arcs first, since they may be subsumed by other arcs.\n" +
            "  arcs.forEach(function(i, j) {\n" +
            "    var arc = topology.arcs[i < 0 ? ~i : i], t;\n" +
            "    if (arc.length < 3 && !arc[1][0] && !arc[1][1]) {\n" +
            "      t = arcs[++emptyIndex], arcs[emptyIndex] = i, arcs[j] = t;\n" +
            "    }\n" +
            "  });\n" +
            "\n" +
            "  arcs.forEach(function(i) {\n" +
            "    var e = ends(i),\n" +
            "        start = e[0],\n" +
            "        end = e[1],\n" +
            "        f, g;\n" +
            "\n" +
            "    if (f = fragmentByEnd[start]) {\n" +
            "      delete fragmentByEnd[f.end];\n" +
            "      f.push(i);\n" +
            "      f.end = end;\n" +
            "      if (g = fragmentByStart[end]) {\n" +
            "        delete fragmentByStart[g.start];\n" +
            "        var fg = g === f ? f : f.concat(g);\n" +
            "        fragmentByStart[fg.start = f.start] = fragmentByEnd[fg.end = g.end] = fg;\n" +
            "      } else {\n" +
            "        fragmentByStart[f.start] = fragmentByEnd[f.end] = f;\n" +
            "      }\n" +
            "    } else if (f = fragmentByStart[end]) {\n" +
            "      delete fragmentByStart[f.start];\n" +
            "      f.unshift(i);\n" +
            "      f.start = start;\n" +
            "      if (g = fragmentByEnd[start]) {\n" +
            "        delete fragmentByEnd[g.end];\n" +
            "        var gf = g === f ? f : g.concat(f);\n" +
            "        fragmentByStart[gf.start = g.start] = fragmentByEnd[gf.end = f.end] = gf;\n" +
            "      } else {\n" +
            "        fragmentByStart[f.start] = fragmentByEnd[f.end] = f;\n" +
            "      }\n" +
            "    } else {\n" +
            "      f = [i];\n" +
            "      fragmentByStart[f.start = start] = fragmentByEnd[f.end = end] = f;\n" +
            "    }\n" +
            "  });\n" +
            "\n" +
            "  function ends(i) {\n" +
            "    var arc = topology.arcs[i < 0 ? ~i : i], p0 = arc[0], p1;\n" +
            "    if (topology.transform) p1 = [0, 0], arc.forEach(function(dp) { p1[0] += dp[0], p1[1] += dp[1]; });\n" +
            "    else p1 = arc[arc.length - 1];\n" +
            "    return i < 0 ? [p1, p0] : [p0, p1];\n" +
            "  }\n" +
            "\n" +
            "  function flush(fragmentByEnd, fragmentByStart) {\n" +
            "    for (var k in fragmentByEnd) {\n" +
            "      var f = fragmentByEnd[k];\n" +
            "      delete fragmentByStart[f.start];\n" +
            "      delete f.start;\n" +
            "      delete f.end;\n" +
            "      f.forEach(function(i) { stitchedArcs[i < 0 ? ~i : i] = 1; });\n" +
            "      fragments.push(f);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  flush(fragmentByEnd, fragmentByStart);\n" +
            "  flush(fragmentByStart, fragmentByEnd);\n" +
            "  arcs.forEach(function(i) { if (!stitchedArcs[i < 0 ? ~i : i]) fragments.push([i]); });\n" +
            "\n" +
            "  return fragments;\n" +
            "};\n" +
            "\n" +
            "var mesh = function(topology) {\n" +
            "  return object(topology, meshArcs.apply(this, arguments));\n" +
            "};\n" +
            "\n" +
            "function meshArcs(topology, object$$1, filter) {\n" +
            "  var arcs, i, n;\n" +
            "  if (arguments.length > 1) arcs = extractArcs(topology, object$$1, filter);\n" +
            "  else for (i = 0, arcs = new Array(n = topology.arcs.length); i < n; ++i) arcs[i] = i;\n" +
            "  return {type: \"MultiLineString\", arcs: stitch(topology, arcs)};\n" +
            "}\n" +
            "\n" +
            "function extractArcs(topology, object$$1, filter) {\n" +
            "  var arcs = [],\n" +
            "      geomsByArc = [],\n" +
            "      geom;\n" +
            "\n" +
            "  function extract0(i) {\n" +
            "    var j = i < 0 ? ~i : i;\n" +
            "    (geomsByArc[j] || (geomsByArc[j] = [])).push({i: i, g: geom});\n" +
            "  }\n" +
            "\n" +
            "  function extract1(arcs) {\n" +
            "    arcs.forEach(extract0);\n" +
            "  }\n" +
            "\n" +
            "  function extract2(arcs) {\n" +
            "    arcs.forEach(extract1);\n" +
            "  }\n" +
            "\n" +
            "  function extract3(arcs) {\n" +
            "    arcs.forEach(extract2);\n" +
            "  }\n" +
            "\n" +
            "  function geometry(o) {\n" +
            "    switch (geom = o, o.type) {\n" +
            "      case \"GeometryCollection\": o.geometries.forEach(geometry); break;\n" +
            "      case \"LineString\": extract1(o.arcs); break;\n" +
            "      case \"MultiLineString\": case \"Polygon\": extract2(o.arcs); break;\n" +
            "      case \"MultiPolygon\": extract3(o.arcs); break;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  geometry(object$$1);\n" +
            "\n" +
            "  geomsByArc.forEach(filter == null\n" +
            "      ? function(geoms) { arcs.push(geoms[0].i); }\n" +
            "      : function(geoms) { if (filter(geoms[0].g, geoms[geoms.length - 1].g)) arcs.push(geoms[0].i); });\n" +
            "\n" +
            "  return arcs;\n" +
            "}\n" +
            "\n" +
            "function planarRingArea(ring) {\n" +
            "  var i = -1, n = ring.length, a, b = ring[n - 1], area = 0;\n" +
            "  while (++i < n) a = b, b = ring[i], area += a[0] * b[1] - a[1] * b[0];\n" +
            "  return Math.abs(area); // Note: doubled area!\n" +
            "}\n" +
            "\n" +
            "var merge = function(topology) {\n" +
            "  return object(topology, mergeArcs.apply(this, arguments));\n" +
            "};\n" +
            "\n" +
            "function mergeArcs(topology, objects) {\n" +
            "  var polygonsByArc = {},\n" +
            "      polygons = [],\n" +
            "      groups = [];\n" +
            "\n" +
            "  objects.forEach(geometry);\n" +
            "\n" +
            "  function geometry(o) {\n" +
            "    switch (o.type) {\n" +
            "      case \"GeometryCollection\": o.geometries.forEach(geometry); break;\n" +
            "      case \"Polygon\": extract(o.arcs); break;\n" +
            "      case \"MultiPolygon\": o.arcs.forEach(extract); break;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  function extract(polygon) {\n" +
            "    polygon.forEach(function(ring) {\n" +
            "      ring.forEach(function(arc) {\n" +
            "        (polygonsByArc[arc = arc < 0 ? ~arc : arc] || (polygonsByArc[arc] = [])).push(polygon);\n" +
            "      });\n" +
            "    });\n" +
            "    polygons.push(polygon);\n" +
            "  }\n" +
            "\n" +
            "  function area(ring) {\n" +
            "    return planarRingArea(object(topology, {type: \"Polygon\", arcs: [ring]}).coordinates[0]);\n" +
            "  }\n" +
            "\n" +
            "  polygons.forEach(function(polygon) {\n" +
            "    if (!polygon._) {\n" +
            "      var group = [],\n" +
            "          neighbors = [polygon];\n" +
            "      polygon._ = 1;\n" +
            "      groups.push(group);\n" +
            "      while (polygon = neighbors.pop()) {\n" +
            "        group.push(polygon);\n" +
            "        polygon.forEach(function(ring) {\n" +
            "          ring.forEach(function(arc) {\n" +
            "            polygonsByArc[arc < 0 ? ~arc : arc].forEach(function(polygon) {\n" +
            "              if (!polygon._) {\n" +
            "                polygon._ = 1;\n" +
            "                neighbors.push(polygon);\n" +
            "              }\n" +
            "            });\n" +
            "          });\n" +
            "        });\n" +
            "      }\n" +
            "    }\n" +
            "  });\n" +
            "\n" +
            "  polygons.forEach(function(polygon) {\n" +
            "    delete polygon._;\n" +
            "  });\n" +
            "\n" +
            "  return {\n" +
            "    type: \"MultiPolygon\",\n" +
            "    arcs: groups.map(function(polygons) {\n" +
            "      var arcs = [], n;\n" +
            "\n" +
            "      // Extract the exterior (unique) arcs.\n" +
            "      polygons.forEach(function(polygon) {\n" +
            "        polygon.forEach(function(ring) {\n" +
            "          ring.forEach(function(arc) {\n" +
            "            if (polygonsByArc[arc < 0 ? ~arc : arc].length < 2) {\n" +
            "              arcs.push(arc);\n" +
            "            }\n" +
            "          });\n" +
            "        });\n" +
            "      });\n" +
            "\n" +
            "      // Stitch the arcs into one or more rings.\n" +
            "      arcs = stitch(topology, arcs);\n" +
            "\n" +
            "      // If more than one ring is returned,\n" +
            "      // at most one of these rings can be the exterior;\n" +
            "      // choose the one with the greatest absolute area.\n" +
            "      if ((n = arcs.length) > 1) {\n" +
            "        for (var i = 1, k = area(arcs[0]), ki, t; i < n; ++i) {\n" +
            "          if ((ki = area(arcs[i])) > k) {\n" +
            "            t = arcs[0], arcs[0] = arcs[i], arcs[i] = t, k = ki;\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "\n" +
            "      return arcs;\n" +
            "    })\n" +
            "  };\n" +
            "}\n" +
            "\n" +
            "var bisect = function(a, x) {\n" +
            "  var lo = 0, hi = a.length;\n" +
            "  while (lo < hi) {\n" +
            "    var mid = lo + hi >>> 1;\n" +
            "    if (a[mid] < x) lo = mid + 1;\n" +
            "    else hi = mid;\n" +
            "  }\n" +
            "  return lo;\n" +
            "};\n" +
            "\n" +
            "var neighbors = function(objects) {\n" +
            "  var indexesByArc = {}, // arc index -> array of object indexes\n" +
            "      neighbors = objects.map(function() { return []; });\n" +
            "\n" +
            "  function line(arcs, i) {\n" +
            "    arcs.forEach(function(a) {\n" +
            "      if (a < 0) a = ~a;\n" +
            "      var o = indexesByArc[a];\n" +
            "      if (o) o.push(i);\n" +
            "      else indexesByArc[a] = [i];\n" +
            "    });\n" +
            "  }\n" +
            "\n" +
            "  function polygon(arcs, i) {\n" +
            "    arcs.forEach(function(arc) { line(arc, i); });\n" +
            "  }\n" +
            "\n" +
            "  function geometry(o, i) {\n" +
            "    if (o.type === \"GeometryCollection\") o.geometries.forEach(function(o) { geometry(o, i); });\n" +
            "    else if (o.type in geometryType) geometryType[o.type](o.arcs, i);\n" +
            "  }\n" +
            "\n" +
            "  var geometryType = {\n" +
            "    LineString: line,\n" +
            "    MultiLineString: polygon,\n" +
            "    Polygon: polygon,\n" +
            "    MultiPolygon: function(arcs, i) { arcs.forEach(function(arc) { polygon(arc, i); }); }\n" +
            "  };\n" +
            "\n" +
            "  objects.forEach(geometry);\n" +
            "\n" +
            "  for (var i in indexesByArc) {\n" +
            "    for (var indexes = indexesByArc[i], m = indexes.length, j = 0; j < m; ++j) {\n" +
            "      for (var k = j + 1; k < m; ++k) {\n" +
            "        var ij = indexes[j], ik = indexes[k], n;\n" +
            "        if ((n = neighbors[ij])[i = bisect(n, ik)] !== ik) n.splice(i, 0, ik);\n" +
            "        if ((n = neighbors[ik])[i = bisect(n, ij)] !== ij) n.splice(i, 0, ij);\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  return neighbors;\n" +
            "};\n" +
            "\n" +
            "var untransform = function(transform) {\n" +
            "  if (transform == null) return identity;\n" +
            "  var x0,\n" +
            "      y0,\n" +
            "      kx = transform.scale[0],\n" +
            "      ky = transform.scale[1],\n" +
            "      dx = transform.translate[0],\n" +
            "      dy = transform.translate[1];\n" +
            "  return function(input, i) {\n" +
            "    if (!i) x0 = y0 = 0;\n" +
            "    var j = 2,\n" +
            "        n = input.length,\n" +
            "        output = new Array(n),\n" +
            "        x1 = Math.round((input[0] - dx) / kx),\n" +
            "        y1 = Math.round((input[1] - dy) / ky);\n" +
            "    output[0] = x1 - x0, x0 = x1;\n" +
            "    output[1] = y1 - y0, y0 = y1;\n" +
            "    while (j < n) output[j] = input[j], ++j;\n" +
            "    return output;\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "var quantize = function(topology, transform) {\n" +
            "  if (topology.transform) throw new Error(\"already quantized\");\n" +
            "\n" +
            "  if (!transform || !transform.scale) {\n" +
            "    if (!((n = Math.floor(transform)) >= 2)) throw new Error(\"n must be \\u22652\");\n" +
            "    box = topology.bbox || bbox(topology);\n" +
            "    var x0 = box[0], y0 = box[1], x1 = box[2], y1 = box[3], n;\n" +
            "    transform = {scale: [x1 - x0 ? (x1 - x0) / (n - 1) : 1, y1 - y0 ? (y1 - y0) / (n - 1) : 1], translate: [x0, y0]};\n" +
            "  } else {\n" +
            "    box = topology.bbox;\n" +
            "  }\n" +
            "\n" +
            "  var t = untransform(transform), box, key, inputs = topology.objects, outputs = {};\n" +
            "\n" +
            "  function quantizePoint(point) {\n" +
            "    return t(point);\n" +
            "  }\n" +
            "\n" +
            "  function quantizeGeometry(input) {\n" +
            "    var output;\n" +
            "    switch (input.type) {\n" +
            "      case \"GeometryCollection\": output = {type: \"GeometryCollection\", geometries: input.geometries.map(quantizeGeometry)}; break;\n" +
            "      case \"Point\": output = {type: \"Point\", coordinates: quantizePoint(input.coordinates)}; break;\n" +
            "      case \"MultiPoint\": output = {type: \"MultiPoint\", coordinates: input.coordinates.map(quantizePoint)}; break;\n" +
            "      default: return input;\n" +
            "    }\n" +
            "    if (input.id != null) output.id = input.id;\n" +
            "    if (input.bbox != null) output.bbox = input.bbox;\n" +
            "    if (input.properties != null) output.properties = input.properties;\n" +
            "    return output;\n" +
            "  }\n" +
            "\n" +
            "  function quantizeArc(input) {\n" +
            "    var i = 0, j = 1, n = input.length, p, output = new Array(n); // pessimistic\n" +
            "    output[0] = t(input[0], 0);\n" +
            "    while (++i < n) if ((p = t(input[i], i))[0] || p[1]) output[j++] = p; // non-coincident points\n" +
            "    if (j === 1) output[j++] = [0, 0]; // an arc must have at least two points\n" +
            "    output.length = j;\n" +
            "    return output;\n" +
            "  }\n" +
            "\n" +
            "  for (key in inputs) outputs[key] = quantizeGeometry(inputs[key]);\n" +
            "\n" +
            "  return {\n" +
            "    type: \"Topology\",\n" +
            "    bbox: box,\n" +
            "    transform: transform,\n" +
            "    objects: outputs,\n" +
            "    arcs: topology.arcs.map(quantizeArc)\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "// Computes the bounding box of the specified hash of GeoJSON objects.\n" +
            "var bounds = function(objects) {\n" +
            "  var x0 = Infinity,\n" +
            "      y0 = Infinity,\n" +
            "      x1 = -Infinity,\n" +
            "      y1 = -Infinity;\n" +
            "\n" +
            "  function boundGeometry(geometry) {\n" +
            "    if (geometry != null && boundGeometryType.hasOwnProperty(geometry.type)) boundGeometryType[geometry.type](geometry);\n" +
            "  }\n" +
            "\n" +
            "  var boundGeometryType = {\n" +
            "    GeometryCollection: function(o) { o.geometries.forEach(boundGeometry); },\n" +
            "    Point: function(o) { boundPoint(o.coordinates); },\n" +
            "    MultiPoint: function(o) { o.coordinates.forEach(boundPoint); },\n" +
            "    LineString: function(o) { boundLine(o.arcs); },\n" +
            "    MultiLineString: function(o) { o.arcs.forEach(boundLine); },\n" +
            "    Polygon: function(o) { o.arcs.forEach(boundLine); },\n" +
            "    MultiPolygon: function(o) { o.arcs.forEach(boundMultiLine); }\n" +
            "  };\n" +
            "\n" +
            "  function boundPoint(coordinates) {\n" +
            "    var x = coordinates[0],\n" +
            "        y = coordinates[1];\n" +
            "    if (x < x0) x0 = x;\n" +
            "    if (x > x1) x1 = x;\n" +
            "    if (y < y0) y0 = y;\n" +
            "    if (y > y1) y1 = y;\n" +
            "  }\n" +
            "\n" +
            "  function boundLine(coordinates) {\n" +
            "    coordinates.forEach(boundPoint);\n" +
            "  }\n" +
            "\n" +
            "  function boundMultiLine(coordinates) {\n" +
            "    coordinates.forEach(boundLine);\n" +
            "  }\n" +
            "\n" +
            "  for (var key in objects) {\n" +
            "    boundGeometry(objects[key]);\n" +
            "  }\n" +
            "\n" +
            "  return x1 >= x0 && y1 >= y0 ? [x0, y0, x1, y1] : undefined;\n" +
            "};\n" +
            "\n" +
            "var hashset = function(size, hash, equal, type, empty) {\n" +
            "  if (arguments.length === 3) {\n" +
            "    type = Array;\n" +
            "    empty = null;\n" +
            "  }\n" +
            "\n" +
            "  var store = new type(size = 1 << Math.max(4, Math.ceil(Math.log(size) / Math.LN2))),\n" +
            "      mask = size - 1;\n" +
            "\n" +
            "  for (var i = 0; i < size; ++i) {\n" +
            "    store[i] = empty;\n" +
            "  }\n" +
            "\n" +
            "  function add(value) {\n" +
            "    var index = hash(value) & mask,\n" +
            "        match = store[index],\n" +
            "        collisions = 0;\n" +
            "    while (match != empty) {\n" +
            "      if (equal(match, value)) return true;\n" +
            "      if (++collisions >= size) throw new Error(\"full hashset\");\n" +
            "      match = store[index = (index + 1) & mask];\n" +
            "    }\n" +
            "    store[index] = value;\n" +
            "    return true;\n" +
            "  }\n" +
            "\n" +
            "  function has(value) {\n" +
            "    var index = hash(value) & mask,\n" +
            "        match = store[index],\n" +
            "        collisions = 0;\n" +
            "    while (match != empty) {\n" +
            "      if (equal(match, value)) return true;\n" +
            "      if (++collisions >= size) break;\n" +
            "      match = store[index = (index + 1) & mask];\n" +
            "    }\n" +
            "    return false;\n" +
            "  }\n" +
            "\n" +
            "  function values() {\n" +
            "    var values = [];\n" +
            "    for (var i = 0, n = store.length; i < n; ++i) {\n" +
            "      var match = store[i];\n" +
            "      if (match != empty) values.push(match);\n" +
            "    }\n" +
            "    return values;\n" +
            "  }\n" +
            "\n" +
            "  return {\n" +
            "    add: add,\n" +
            "    has: has,\n" +
            "    values: values\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "var hashmap = function(size, hash, equal, keyType, keyEmpty, valueType) {\n" +
            "  if (arguments.length === 3) {\n" +
            "    keyType = valueType = Array;\n" +
            "    keyEmpty = null;\n" +
            "  }\n" +
            "\n" +
            "  var keystore = new keyType(size = 1 << Math.max(4, Math.ceil(Math.log(size) / Math.LN2))),\n" +
            "      valstore = new valueType(size),\n" +
            "      mask = size - 1;\n" +
            "\n" +
            "  for (var i = 0; i < size; ++i) {\n" +
            "    keystore[i] = keyEmpty;\n" +
            "  }\n" +
            "\n" +
            "  function set(key, value) {\n" +
            "    var index = hash(key) & mask,\n" +
            "        matchKey = keystore[index],\n" +
            "        collisions = 0;\n" +
            "    while (matchKey != keyEmpty) {\n" +
            "      if (equal(matchKey, key)) return valstore[index] = value;\n" +
            "      if (++collisions >= size) throw new Error(\"full hashmap\");\n" +
            "      matchKey = keystore[index = (index + 1) & mask];\n" +
            "    }\n" +
            "    keystore[index] = key;\n" +
            "    valstore[index] = value;\n" +
            "    return value;\n" +
            "  }\n" +
            "\n" +
            "  function maybeSet(key, value) {\n" +
            "    var index = hash(key) & mask,\n" +
            "        matchKey = keystore[index],\n" +
            "        collisions = 0;\n" +
            "    while (matchKey != keyEmpty) {\n" +
            "      if (equal(matchKey, key)) return valstore[index];\n" +
            "      if (++collisions >= size) throw new Error(\"full hashmap\");\n" +
            "      matchKey = keystore[index = (index + 1) & mask];\n" +
            "    }\n" +
            "    keystore[index] = key;\n" +
            "    valstore[index] = value;\n" +
            "    return value;\n" +
            "  }\n" +
            "\n" +
            "  function get(key, missingValue) {\n" +
            "    var index = hash(key) & mask,\n" +
            "        matchKey = keystore[index],\n" +
            "        collisions = 0;\n" +
            "    while (matchKey != keyEmpty) {\n" +
            "      if (equal(matchKey, key)) return valstore[index];\n" +
            "      if (++collisions >= size) break;\n" +
            "      matchKey = keystore[index = (index + 1) & mask];\n" +
            "    }\n" +
            "    return missingValue;\n" +
            "  }\n" +
            "\n" +
            "  function keys() {\n" +
            "    var keys = [];\n" +
            "    for (var i = 0, n = keystore.length; i < n; ++i) {\n" +
            "      var matchKey = keystore[i];\n" +
            "      if (matchKey != keyEmpty) keys.push(matchKey);\n" +
            "    }\n" +
            "    return keys;\n" +
            "  }\n" +
            "\n" +
            "  return {\n" +
            "    set: set,\n" +
            "    maybeSet: maybeSet, // set if unset\n" +
            "    get: get,\n" +
            "    keys: keys\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "var equalPoint = function(pointA, pointB) {\n" +
            "  return pointA[0] === pointB[0] && pointA[1] === pointB[1];\n" +
            "};\n" +
            "\n" +
            "// TODO if quantized, use simpler Int32 hashing?\n" +
            "\n" +
            "var buffer = new ArrayBuffer(16);\n" +
            "var uints = new Uint32Array(buffer);\n" +
            "\n" +
            "var hashPoint = function(point) {\n" +
            "  var hash = uints[0] ^ uints[1];\n" +
            "  hash = hash << 5 ^ hash >> 7 ^ uints[2] ^ uints[3];\n" +
            "  return hash & 0x7fffffff;\n" +
            "};\n" +
            "\n" +
            "// Given an extracted (pre-)topology, identifies all of the junctions. These are\n" +
            "// the points at which arcs (lines or rings) will need to be cut so that each\n" +
            "// arc is represented uniquely.\n" +
            "//\n" +
            "// A junction is a point where at least one arc deviates from another arc going\n" +
            "// through the same point. For example, consider the point B. If there is a arc\n" +
            "// through ABC and another arc through CBA, then B is not a junction because in\n" +
            "// both cases the adjacent point pairs are {A,C}. However, if there is an\n" +
            "// additional arc ABD, then {A,D} != {A,C}, and thus B becomes a junction.\n" +
            "//\n" +
            "// For a closed ring ABCA, the first point A��s adjacent points are the second\n" +
            "// and last point {B,C}. For a line, the first and last point are always\n" +
            "// considered junctions, even if the line is closed; this ensures that a closed\n" +
            "// line is never rotated.\n" +
            "var join = function(topology) {\n" +
            "  var coordinates = topology.coordinates,\n" +
            "      lines = topology.lines,\n" +
            "      rings = topology.rings,\n" +
            "      indexes = index(),\n" +
            "      visitedByIndex = new Int32Array(coordinates.length),\n" +
            "      leftByIndex = new Int32Array(coordinates.length),\n" +
            "      rightByIndex = new Int32Array(coordinates.length),\n" +
            "      junctionByIndex = new Int8Array(coordinates.length),\n" +
            "      junctionCount = 0, // upper bound on number of junctions\n" +
            "      i, n,\n" +
            "      previousIndex,\n" +
            "      currentIndex,\n" +
            "      nextIndex;\n" +
            "\n" +
            "  for (i = 0, n = coordinates.length; i < n; ++i) {\n" +
            "    visitedByIndex[i] = leftByIndex[i] = rightByIndex[i] = -1;\n" +
            "  }\n" +
            "\n" +
            "  for (i = 0, n = lines.length; i < n; ++i) {\n" +
            "    var line = lines[i],\n" +
            "        lineStart = line[0],\n" +
            "        lineEnd = line[1];\n" +
            "    currentIndex = indexes[lineStart];\n" +
            "    nextIndex = indexes[++lineStart];\n" +
            "    ++junctionCount, junctionByIndex[currentIndex] = 1; // start\n" +
            "    while (++lineStart <= lineEnd) {\n" +
            "      sequence(i, previousIndex = currentIndex, currentIndex = nextIndex, nextIndex = indexes[lineStart]);\n" +
            "    }\n" +
            "    ++junctionCount, junctionByIndex[nextIndex] = 1; // end\n" +
            "  }\n" +
            "\n" +
            "  for (i = 0, n = coordinates.length; i < n; ++i) {\n" +
            "    visitedByIndex[i] = -1;\n" +
            "  }\n" +
            "\n" +
            "  for (i = 0, n = rings.length; i < n; ++i) {\n" +
            "    var ring = rings[i],\n" +
            "        ringStart = ring[0] + 1,\n" +
            "        ringEnd = ring[1];\n" +
            "    previousIndex = indexes[ringEnd - 1];\n" +
            "    currentIndex = indexes[ringStart - 1];\n" +
            "    nextIndex = indexes[ringStart];\n" +
            "    sequence(i, previousIndex, currentIndex, nextIndex);\n" +
            "    while (++ringStart <= ringEnd) {\n" +
            "      sequence(i, previousIndex = currentIndex, currentIndex = nextIndex, nextIndex = indexes[ringStart]);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  function sequence(i, previousIndex, currentIndex, nextIndex) {\n" +
            "    if (visitedByIndex[currentIndex] === i) return; // ignore self-intersection\n" +
            "    visitedByIndex[currentIndex] = i;\n" +
            "    var leftIndex = leftByIndex[currentIndex];\n" +
            "    if (leftIndex >= 0) {\n" +
            "      var rightIndex = rightByIndex[currentIndex];\n" +
            "      if ((leftIndex !== previousIndex || rightIndex !== nextIndex)\n" +
            "        && (leftIndex !== nextIndex || rightIndex !== previousIndex)) {\n" +
            "        ++junctionCount, junctionByIndex[currentIndex] = 1;\n" +
            "      }\n" +
            "    } else {\n" +
            "      leftByIndex[currentIndex] = previousIndex;\n" +
            "      rightByIndex[currentIndex] = nextIndex;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  function index() {\n" +
            "    var indexByPoint = hashmap(coordinates.length * 1.4, hashIndex, equalIndex, Int32Array, -1, Int32Array),\n" +
            "        indexes = new Int32Array(coordinates.length);\n" +
            "\n" +
            "    for (var i = 0, n = coordinates.length; i < n; ++i) {\n" +
            "      indexes[i] = indexByPoint.maybeSet(i, i);\n" +
            "    }\n" +
            "\n" +
            "    return indexes;\n" +
            "  }\n" +
            "\n" +
            "  function hashIndex(i) {\n" +
            "    return hashPoint(coordinates[i]);\n" +
            "  }\n" +
            "\n" +
            "  function equalIndex(i, j) {\n" +
            "    return equalPoint(coordinates[i], coordinates[j]);\n" +
            "  }\n" +
            "\n" +
            "  visitedByIndex = leftByIndex = rightByIndex = null;\n" +
            "\n" +
            "  var junctionByPoint = hashset(junctionCount * 1.4, hashPoint, equalPoint), j;\n" +
            "\n" +
            "  // Convert back to a standard hashset by point for caller convenience.\n" +
            "  for (i = 0, n = coordinates.length; i < n; ++i) {\n" +
            "    if (junctionByIndex[j = indexes[i]]) {\n" +
            "      junctionByPoint.add(coordinates[j]);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  return junctionByPoint;\n" +
            "};\n" +
            "\n" +
            "// Given an extracted (pre-)topology, cuts (or rotates) arcs so that all shared\n" +
            "// point sequences are identified. The topology can then be subsequently deduped\n" +
            "// to remove exact duplicate arcs.\n" +
            "var cut = function(topology) {\n" +
            "  var junctions = join(topology),\n" +
            "      coordinates = topology.coordinates,\n" +
            "      lines = topology.lines,\n" +
            "      rings = topology.rings,\n" +
            "      next,\n" +
            "      i, n;\n" +
            "\n" +
            "  for (i = 0, n = lines.length; i < n; ++i) {\n" +
            "    var line = lines[i],\n" +
            "        lineMid = line[0],\n" +
            "        lineEnd = line[1];\n" +
            "    while (++lineMid < lineEnd) {\n" +
            "      if (junctions.has(coordinates[lineMid])) {\n" +
            "        next = {0: lineMid, 1: line[1]};\n" +
            "        line[1] = lineMid;\n" +
            "        line = line.next = next;\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  for (i = 0, n = rings.length; i < n; ++i) {\n" +
            "    var ring = rings[i],\n" +
            "        ringStart = ring[0],\n" +
            "        ringMid = ringStart,\n" +
            "        ringEnd = ring[1],\n" +
            "        ringFixed = junctions.has(coordinates[ringStart]);\n" +
            "    while (++ringMid < ringEnd) {\n" +
            "      if (junctions.has(coordinates[ringMid])) {\n" +
            "        if (ringFixed) {\n" +
            "          next = {0: ringMid, 1: ring[1]};\n" +
            "          ring[1] = ringMid;\n" +
            "          ring = ring.next = next;\n" +
            "        } else { // For the first junction, we can rotate rather than cut.\n" +
            "          rotateArray(coordinates, ringStart, ringEnd, ringEnd - ringMid);\n" +
            "          coordinates[ringEnd] = coordinates[ringStart];\n" +
            "          ringFixed = true;\n" +
            "          ringMid = ringStart; // restart; we may have skipped junctions\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  return topology;\n" +
            "};\n" +
            "\n" +
            "function rotateArray(array, start, end, offset) {\n" +
            "  reverse$1(array, start, end);\n" +
            "  reverse$1(array, start, start + offset);\n" +
            "  reverse$1(array, start + offset, end);\n" +
            "}\n" +
            "\n" +
            "function reverse$1(array, start, end) {\n" +
            "  for (var mid = start + ((end-- - start) >> 1), t; start < mid; ++start, --end) {\n" +
            "    t = array[start], array[start] = array[end], array[end] = t;\n" +
            "  }\n" +
            "}\n" +
            "\n" +
            "// Given a cut topology, combines duplicate arcs.\n" +
            "var dedup = function(topology) {\n" +
            "  var coordinates = topology.coordinates,\n" +
            "      lines = topology.lines, line,\n" +
            "      rings = topology.rings, ring,\n" +
            "      arcCount = lines.length + rings.length,\n" +
            "      i, n;\n" +
            "\n" +
            "  delete topology.lines;\n" +
            "  delete topology.rings;\n" +
            "\n" +
            "  // Count the number of (non-unique) arcs to initialize the hashmap safely.\n" +
            "  for (i = 0, n = lines.length; i < n; ++i) {\n" +
            "    line = lines[i]; while (line = line.next) ++arcCount;\n" +
            "  }\n" +
            "  for (i = 0, n = rings.length; i < n; ++i) {\n" +
            "    ring = rings[i]; while (ring = ring.next) ++arcCount;\n" +
            "  }\n" +
            "\n" +
            "  var arcsByEnd = hashmap(arcCount * 2 * 1.4, hashPoint, equalPoint),\n" +
            "      arcs = topology.arcs = [];\n" +
            "\n" +
            "  for (i = 0, n = lines.length; i < n; ++i) {\n" +
            "    line = lines[i];\n" +
            "    do {\n" +
            "      dedupLine(line);\n" +
            "    } while (line = line.next);\n" +
            "  }\n" +
            "\n" +
            "  for (i = 0, n = rings.length; i < n; ++i) {\n" +
            "    ring = rings[i];\n" +
            "    if (ring.next) { // arc is no longer closed\n" +
            "      do {\n" +
            "        dedupLine(ring);\n" +
            "      } while (ring = ring.next);\n" +
            "    } else {\n" +
            "      dedupRing(ring);\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  function dedupLine(arc) {\n" +
            "    var startPoint,\n" +
            "        endPoint,\n" +
            "        startArcs, startArc,\n" +
            "        endArcs, endArc,\n" +
            "        i, n;\n" +
            "\n" +
            "    // Does this arc match an existing arc in order?\n" +
            "    if (startArcs = arcsByEnd.get(startPoint = coordinates[arc[0]])) {\n" +
            "      for (i = 0, n = startArcs.length; i < n; ++i) {\n" +
            "        startArc = startArcs[i];\n" +
            "        if (equalLine(startArc, arc)) {\n" +
            "          arc[0] = startArc[0];\n" +
            "          arc[1] = startArc[1];\n" +
            "          return;\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    // Does this arc match an existing arc in reverse order?\n" +
            "    if (endArcs = arcsByEnd.get(endPoint = coordinates[arc[1]])) {\n" +
            "      for (i = 0, n = endArcs.length; i < n; ++i) {\n" +
            "        endArc = endArcs[i];\n" +
            "        if (reverseEqualLine(endArc, arc)) {\n" +
            "          arc[1] = endArc[0];\n" +
            "          arc[0] = endArc[1];\n" +
            "          return;\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    if (startArcs) startArcs.push(arc); else arcsByEnd.set(startPoint, [arc]);\n" +
            "    if (endArcs) endArcs.push(arc); else arcsByEnd.set(endPoint, [arc]);\n" +
            "    arcs.push(arc);\n" +
            "  }\n" +
            "\n" +
            "  function dedupRing(arc) {\n" +
            "    var endPoint,\n" +
            "        endArcs,\n" +
            "        endArc,\n" +
            "        i, n;\n" +
            "\n" +
            "    // Does this arc match an existing line in order, or reverse order?\n" +
            "    // Rings are closed, so their start point and end point is the same.\n" +
            "    if (endArcs = arcsByEnd.get(endPoint = coordinates[arc[0]])) {\n" +
            "      for (i = 0, n = endArcs.length; i < n; ++i) {\n" +
            "        endArc = endArcs[i];\n" +
            "        if (equalRing(endArc, arc)) {\n" +
            "          arc[0] = endArc[0];\n" +
            "          arc[1] = endArc[1];\n" +
            "          return;\n" +
            "        }\n" +
            "        if (reverseEqualRing(endArc, arc)) {\n" +
            "          arc[0] = endArc[1];\n" +
            "          arc[1] = endArc[0];\n" +
            "          return;\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    // Otherwise, does this arc match an existing ring in order, or reverse order?\n" +
            "    if (endArcs = arcsByEnd.get(endPoint = coordinates[arc[0] + findMinimumOffset(arc)])) {\n" +
            "      for (i = 0, n = endArcs.length; i < n; ++i) {\n" +
            "        endArc = endArcs[i];\n" +
            "        if (equalRing(endArc, arc)) {\n" +
            "          arc[0] = endArc[0];\n" +
            "          arc[1] = endArc[1];\n" +
            "          return;\n" +
            "        }\n" +
            "        if (reverseEqualRing(endArc, arc)) {\n" +
            "          arc[0] = endArc[1];\n" +
            "          arc[1] = endArc[0];\n" +
            "          return;\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    if (endArcs) endArcs.push(arc); else arcsByEnd.set(endPoint, [arc]);\n" +
            "    arcs.push(arc);\n" +
            "  }\n" +
            "\n" +
            "  function equalLine(arcA, arcB) {\n" +
            "    var ia = arcA[0], ib = arcB[0],\n" +
            "        ja = arcA[1], jb = arcB[1];\n" +
            "    if (ia - ja !== ib - jb) return false;\n" +
            "    for (; ia <= ja; ++ia, ++ib) if (!equalPoint(coordinates[ia], coordinates[ib])) return false;\n" +
            "    return true;\n" +
            "  }\n" +
            "\n" +
            "  function reverseEqualLine(arcA, arcB) {\n" +
            "    var ia = arcA[0], ib = arcB[0],\n" +
            "        ja = arcA[1], jb = arcB[1];\n" +
            "    if (ia - ja !== ib - jb) return false;\n" +
            "    for (; ia <= ja; ++ia, --jb) if (!equalPoint(coordinates[ia], coordinates[jb])) return false;\n" +
            "    return true;\n" +
            "  }\n" +
            "\n" +
            "  function equalRing(arcA, arcB) {\n" +
            "    var ia = arcA[0], ib = arcB[0],\n" +
            "        ja = arcA[1], jb = arcB[1],\n" +
            "        n = ja - ia;\n" +
            "    if (n !== jb - ib) return false;\n" +
            "    var ka = findMinimumOffset(arcA),\n" +
            "        kb = findMinimumOffset(arcB);\n" +
            "    for (var i = 0; i < n; ++i) {\n" +
            "      if (!equalPoint(coordinates[ia + (i + ka) % n], coordinates[ib + (i + kb) % n])) return false;\n" +
            "    }\n" +
            "    return true;\n" +
            "  }\n" +
            "\n" +
            "  function reverseEqualRing(arcA, arcB) {\n" +
            "    var ia = arcA[0], ib = arcB[0],\n" +
            "        ja = arcA[1], jb = arcB[1],\n" +
            "        n = ja - ia;\n" +
            "    if (n !== jb - ib) return false;\n" +
            "    var ka = findMinimumOffset(arcA),\n" +
            "        kb = n - findMinimumOffset(arcB);\n" +
            "    for (var i = 0; i < n; ++i) {\n" +
            "      if (!equalPoint(coordinates[ia + (i + ka) % n], coordinates[jb - (i + kb) % n])) return false;\n" +
            "    }\n" +
            "    return true;\n" +
            "  }\n" +
            "\n" +
            "  // Rings are rotated to a consistent, but arbitrary, start point.\n" +
            "  // This is necessary to detect when a ring and a rotated copy are dupes.\n" +
            "  function findMinimumOffset(arc) {\n" +
            "    var start = arc[0],\n" +
            "        end = arc[1],\n" +
            "        mid = start,\n" +
            "        minimum = mid,\n" +
            "        minimumPoint = coordinates[mid];\n" +
            "    while (++mid < end) {\n" +
            "      var point = coordinates[mid];\n" +
            "      if (point[0] < minimumPoint[0] || point[0] === minimumPoint[0] && point[1] < minimumPoint[1]) {\n" +
            "        minimum = mid;\n" +
            "        minimumPoint = point;\n" +
            "      }\n" +
            "    }\n" +
            "    return minimum - start;\n" +
            "  }\n" +
            "\n" +
            "  return topology;\n" +
            "};\n" +
            "\n" +
            "// Given an array of arcs in absolute (but already quantized!) coordinates,\n" +
            "// converts to fixed-point delta encoding.\n" +
            "// This is a destructive operation that modifies the given arcs!\n" +
            "var delta = function(arcs) {\n" +
            "  var i = -1,\n" +
            "      n = arcs.length;\n" +
            "\n" +
            "  while (++i < n) {\n" +
            "    var arc = arcs[i],\n" +
            "        j = 0,\n" +
            "        k = 1,\n" +
            "        m = arc.length,\n" +
            "        point = arc[0],\n" +
            "        x0 = point[0],\n" +
            "        y0 = point[1],\n" +
            "        x1,\n" +
            "        y1;\n" +
            "\n" +
            "    while (++j < m) {\n" +
            "      point = arc[j], x1 = point[0], y1 = point[1];\n" +
            "      if (x1 !== x0 || y1 !== y0) arc[k++] = [x1 - x0, y1 - y0], x0 = x1, y0 = y1;\n" +
            "    }\n" +
            "\n" +
            "    if (k === 1) arc[k++] = [0, 0]; // Each arc must be an array of two or more positions.\n" +
            "\n" +
            "    arc.length = k;\n" +
            "  }\n" +
            "\n" +
            "  return arcs;\n" +
            "};\n" +
            "\n" +
            "// Extracts the lines and rings from the specified hash of geometry objects.\n" +
            "//\n" +
            "// Returns an object with three properties:\n" +
            "//\n" +
            "// * coordinates - shared buffer of [x, y] coordinates\n" +
            "// * lines - lines extracted from the hash, of the form [start, end]\n" +
            "// * rings - rings extracted from the hash, of the form [start, end]\n" +
            "//\n" +
            "// For each ring or line, start and end represent inclusive indexes into the\n" +
            "// coordinates buffer. For rings (and closed lines), coordinates[start] equals\n" +
            "// coordinates[end].\n" +
            "//\n" +
            "// For each line or polygon geometry in the input hash, including nested\n" +
            "// geometries as in geometry collections, the `coordinates` array is replaced\n" +
            "// with an equivalent `arcs` array that, for each line (for line string\n" +
            "// geometries) or ring (for polygon geometries), points to one of the above\n" +
            "// lines or rings.\n" +
            "var extract = function(objects) {\n" +
            "  var index = -1,\n" +
            "      lines = [],\n" +
            "      rings = [],\n" +
            "      coordinates = [];\n" +
            "\n" +
            "  function extractGeometry(geometry) {\n" +
            "    if (geometry && extractGeometryType.hasOwnProperty(geometry.type)) extractGeometryType[geometry.type](geometry);\n" +
            "  }\n" +
            "\n" +
            "  var extractGeometryType = {\n" +
            "    GeometryCollection: function(o) { o.geometries.forEach(extractGeometry); },\n" +
            "    LineString: function(o) { o.arcs = extractLine(o.arcs); },\n" +
            "    MultiLineString: function(o) { o.arcs = o.arcs.map(extractLine); },\n" +
            "    Polygon: function(o) { o.arcs = o.arcs.map(extractRing); },\n" +
            "    MultiPolygon: function(o) { o.arcs = o.arcs.map(extractMultiRing); }\n" +
            "  };\n" +
            "\n" +
            "  function extractLine(line) {\n" +
            "    for (var i = 0, n = line.length; i < n; ++i) coordinates[++index] = line[i];\n" +
            "    var arc = {0: index - n + 1, 1: index};\n" +
            "    lines.push(arc);\n" +
            "    return arc;\n" +
            "  }\n" +
            "\n" +
            "  function extractRing(ring) {\n" +
            "    for (var i = 0, n = ring.length; i < n; ++i) coordinates[++index] = ring[i];\n" +
            "    var arc = {0: index - n + 1, 1: index};\n" +
            "    rings.push(arc);\n" +
            "    return arc;\n" +
            "  }\n" +
            "\n" +
            "  function extractMultiRing(rings) {\n" +
            "    return rings.map(extractRing);\n" +
            "  }\n" +
            "\n" +
            "  for (var key in objects) {\n" +
            "    extractGeometry(objects[key]);\n" +
            "  }\n" +
            "\n" +
            "  return {\n" +
            "    type: \"Topology\",\n" +
            "    coordinates: coordinates,\n" +
            "    lines: lines,\n" +
            "    rings: rings,\n" +
            "    objects: objects\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "// Given a hash of GeoJSON objects, returns a hash of GeoJSON geometry objects.\n" +
            "// Any null input geometry objects are represented as {type: null} in the output.\n" +
            "// Any feature.{id,properties,bbox} are transferred to the output geometry object.\n" +
            "// Each output geometry object is a shallow copy of the input (e.g., properties, coordinates)!\n" +
            "var geometry = function(inputs) {\n" +
            "  var outputs = {}, key;\n" +
            "  for (key in inputs) outputs[key] = geomifyObject(inputs[key]);\n" +
            "  return outputs;\n" +
            "};\n" +
            "\n" +
            "function geomifyObject(input) {\n" +
            "  return input == null ? {type: null}\n" +
            "      : (input.type === \"FeatureCollection\" ? geomifyFeatureCollection\n" +
            "      : input.type === \"Feature\" ? geomifyFeature\n" +
            "      : geomifyGeometry)(input);\n" +
            "}\n" +
            "\n" +
            "function geomifyFeatureCollection(input) {\n" +
            "  var output = {type: \"GeometryCollection\", geometries: input.features.map(geomifyFeature)};\n" +
            "  if (input.bbox != null) output.bbox = input.bbox;\n" +
            "  return output;\n" +
            "}\n" +
            "\n" +
            "function geomifyFeature(input) {\n" +
            "  var output = geomifyGeometry(input.geometry), key; // eslint-disable-line no-unused-vars\n" +
            "  if (input.id != null) output.id = input.id;\n" +
            "  if (input.bbox != null) output.bbox = input.bbox;\n" +
            "  for (key in input.properties) { output.properties = input.properties; break; }\n" +
            "  return output;\n" +
            "}\n" +
            "\n" +
            "function geomifyGeometry(input) {\n" +
            "  if (input == null) return {type: null};\n" +
            "  var output = input.type === \"GeometryCollection\" ? {type: \"GeometryCollection\", geometries: input.geometries.map(geomifyGeometry)}\n" +
            "      : input.type === \"Point\" || input.type === \"MultiPoint\" ? {type: input.type, coordinates: input.coordinates}\n" +
            "      : {type: input.type, arcs: input.coordinates}; // TODO Check for unknown types?\n" +
            "  if (input.bbox != null) output.bbox = input.bbox;\n" +
            "  return output;\n" +
            "}\n" +
            "\n" +
            "var prequantize = function(objects, bbox, n) {\n" +
            "  var x0 = bbox[0],\n" +
            "      y0 = bbox[1],\n" +
            "      x1 = bbox[2],\n" +
            "      y1 = bbox[3],\n" +
            "      kx = x1 - x0 ? (n - 1) / (x1 - x0) : 1,\n" +
            "      ky = y1 - y0 ? (n - 1) / (y1 - y0) : 1;\n" +
            "\n" +
            "  function quantizePoint(input) {\n" +
            "    return [Math.round((input[0] - x0) * kx), Math.round((input[1] - y0) * ky)];\n" +
            "  }\n" +
            "\n" +
            "  function quantizePoints(input, m) {\n" +
            "    var i = -1,\n" +
            "        j = 0,\n" +
            "        n = input.length,\n" +
            "        output = new Array(n), // pessimistic\n" +
            "        pi,\n" +
            "        px,\n" +
            "        py,\n" +
            "        x,\n" +
            "        y;\n" +
            "\n" +
            "    while (++i < n) {\n" +
            "      pi = input[i];\n" +
            "      x = Math.round((pi[0] - x0) * kx);\n" +
            "      y = Math.round((pi[1] - y0) * ky);\n" +
            "      if (x !== px || y !== py) output[j++] = [px = x, py = y]; // non-coincident points\n" +
            "    }\n" +
            "\n" +
            "    output.length = j;\n" +
            "    while (j < m) j = output.push([output[0][0], output[0][1]]);\n" +
            "    return output;\n" +
            "  }\n" +
            "\n" +
            "  function quantizeLine(input) {\n" +
            "    return quantizePoints(input, 2);\n" +
            "  }\n" +
            "\n" +
            "  function quantizeRing(input) {\n" +
            "    return quantizePoints(input, 4);\n" +
            "  }\n" +
            "\n" +
            "  function quantizePolygon(input) {\n" +
            "    return input.map(quantizeRing);\n" +
            "  }\n" +
            "\n" +
            "  function quantizeGeometry(o) {\n" +
            "    if (o != null && quantizeGeometryType.hasOwnProperty(o.type)) quantizeGeometryType[o.type](o);\n" +
            "  }\n" +
            "\n" +
            "  var quantizeGeometryType = {\n" +
            "    GeometryCollection: function(o) { o.geometries.forEach(quantizeGeometry); },\n" +
            "    Point: function(o) { o.coordinates = quantizePoint(o.coordinates); },\n" +
            "    MultiPoint: function(o) { o.coordinates = o.coordinates.map(quantizePoint); },\n" +
            "    LineString: function(o) { o.arcs = quantizeLine(o.arcs); },\n" +
            "    MultiLineString: function(o) { o.arcs = o.arcs.map(quantizeLine); },\n" +
            "    Polygon: function(o) { o.arcs = quantizePolygon(o.arcs); },\n" +
            "    MultiPolygon: function(o) { o.arcs = o.arcs.map(quantizePolygon); }\n" +
            "  };\n" +
            "\n" +
            "  for (var key in objects) {\n" +
            "    quantizeGeometry(objects[key]);\n" +
            "  }\n" +
            "\n" +
            "  return {\n" +
            "    scale: [1 / kx, 1 / ky],\n" +
            "    translate: [x0, y0]\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "// Constructs the TopoJSON Topology for the specified hash of features.\n" +
            "// Each object in the specified hash must be a GeoJSON object,\n" +
            "// meaning FeatureCollection, a Feature or a geometry object.\n" +
            "var topology = function(objects, quantization) {\n" +
            "  var bbox = bounds(objects = geometry(objects)),\n" +
            "      transform = quantization > 0 && bbox && prequantize(objects, bbox, quantization),\n" +
            "      topology = dedup(cut(extract(objects))),\n" +
            "      coordinates = topology.coordinates,\n" +
            "      indexByArc = hashmap(topology.arcs.length * 1.4, hashArc, equalArc);\n" +
            "\n" +
            "  objects = topology.objects; // for garbage collection\n" +
            "  topology.bbox = bbox;\n" +
            "  topology.arcs = topology.arcs.map(function(arc, i) {\n" +
            "    indexByArc.set(arc, i);\n" +
            "    return coordinates.slice(arc[0], arc[1] + 1);\n" +
            "  });\n" +
            "\n" +
            "  delete topology.coordinates;\n" +
            "  coordinates = null;\n" +
            "\n" +
            "  function indexGeometry(geometry$$1) {\n" +
            "    if (geometry$$1 && indexGeometryType.hasOwnProperty(geometry$$1.type)) indexGeometryType[geometry$$1.type](geometry$$1);\n" +
            "  }\n" +
            "\n" +
            "  var indexGeometryType = {\n" +
            "    GeometryCollection: function(o) { o.geometries.forEach(indexGeometry); },\n" +
            "    LineString: function(o) { o.arcs = indexArcs(o.arcs); },\n" +
            "    MultiLineString: function(o) { o.arcs = o.arcs.map(indexArcs); },\n" +
            "    Polygon: function(o) { o.arcs = o.arcs.map(indexArcs); },\n" +
            "    MultiPolygon: function(o) { o.arcs = o.arcs.map(indexMultiArcs); }\n" +
            "  };\n" +
            "\n" +
            "  function indexArcs(arc) {\n" +
            "    var indexes = [];\n" +
            "    do {\n" +
            "      var index = indexByArc.get(arc);\n" +
            "      indexes.push(arc[0] < arc[1] ? index : ~index);\n" +
            "    } while (arc = arc.next);\n" +
            "    return indexes;\n" +
            "  }\n" +
            "\n" +
            "  function indexMultiArcs(arcs) {\n" +
            "    return arcs.map(indexArcs);\n" +
            "  }\n" +
            "\n" +
            "  for (var key in objects) {\n" +
            "    indexGeometry(objects[key]);\n" +
            "  }\n" +
            "\n" +
            "  if (transform) {\n" +
            "    topology.transform = transform;\n" +
            "    topology.arcs = delta(topology.arcs);\n" +
            "  }\n" +
            "\n" +
            "  return topology;\n" +
            "};\n" +
            "\n" +
            "function hashArc(arc) {\n" +
            "  var i = arc[0], j = arc[1], t;\n" +
            "  if (j < i) t = i, i = j, j = t;\n" +
            "  return i + 31 * j;\n" +
            "}\n" +
            "\n" +
            "function equalArc(arcA, arcB) {\n" +
            "  var ia = arcA[0], ja = arcA[1],\n" +
            "      ib = arcB[0], jb = arcB[1], t;\n" +
            "  if (ja < ia) t = ia, ia = ja, ja = t;\n" +
            "  if (jb < ib) t = ib, ib = jb, jb = t;\n" +
            "  return ia === ib && ja === jb;\n" +
            "}\n" +
            "\n" +
            "var prune = function(topology) {\n" +
            "  var oldObjects = topology.objects,\n" +
            "      newObjects = {},\n" +
            "      oldArcs = topology.arcs,\n" +
            "      oldArcsLength = oldArcs.length,\n" +
            "      oldIndex = -1,\n" +
            "      newIndexByOldIndex = new Array(oldArcsLength),\n" +
            "      newArcsLength = 0,\n" +
            "      newArcs,\n" +
            "      newIndex = -1,\n" +
            "      key;\n" +
            "\n" +
            "  function scanGeometry(input) {\n" +
            "    switch (input.type) {\n" +
            "      case \"GeometryCollection\": input.geometries.forEach(scanGeometry); break;\n" +
            "      case \"LineString\": scanArcs(input.arcs); break;\n" +
            "      case \"MultiLineString\": input.arcs.forEach(scanArcs); break;\n" +
            "      case \"Polygon\": input.arcs.forEach(scanArcs); break;\n" +
            "      case \"MultiPolygon\": input.arcs.forEach(scanMultiArcs); break;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  function scanArc(index) {\n" +
            "    if (index < 0) index = ~index;\n" +
            "    if (!newIndexByOldIndex[index]) newIndexByOldIndex[index] = 1, ++newArcsLength;\n" +
            "  }\n" +
            "\n" +
            "  function scanArcs(arcs) {\n" +
            "    arcs.forEach(scanArc);\n" +
            "  }\n" +
            "\n" +
            "  function scanMultiArcs(arcs) {\n" +
            "    arcs.forEach(scanArcs);\n" +
            "  }\n" +
            "\n" +
            "  function reindexGeometry(input) {\n" +
            "    var output;\n" +
            "    switch (input.type) {\n" +
            "      case \"GeometryCollection\": output = {type: \"GeometryCollection\", geometries: input.geometries.map(reindexGeometry)}; break;\n" +
            "      case \"LineString\": output = {type: \"LineString\", arcs: reindexArcs(input.arcs)}; break;\n" +
            "      case \"MultiLineString\": output = {type: \"MultiLineString\", arcs: input.arcs.map(reindexArcs)}; break;\n" +
            "      case \"Polygon\": output = {type: \"Polygon\", arcs: input.arcs.map(reindexArcs)}; break;\n" +
            "      case \"MultiPolygon\": output = {type: \"MultiPolygon\", arcs: input.arcs.map(reindexMultiArcs)}; break;\n" +
            "      default: return input;\n" +
            "    }\n" +
            "    if (input.id != null) output.id = input.id;\n" +
            "    if (input.bbox != null) output.bbox = input.bbox;\n" +
            "    if (input.properties != null) output.properties = input.properties;\n" +
            "    return output;\n" +
            "  }\n" +
            "\n" +
            "  function reindexArc(oldIndex) {\n" +
            "    return oldIndex < 0 ? ~newIndexByOldIndex[~oldIndex] : newIndexByOldIndex[oldIndex];\n" +
            "  }\n" +
            "\n" +
            "  function reindexArcs(arcs) {\n" +
            "    return arcs.map(reindexArc);\n" +
            "  }\n" +
            "\n" +
            "  function reindexMultiArcs(arcs) {\n" +
            "    return arcs.map(reindexArcs);\n" +
            "  }\n" +
            "\n" +
            "  for (key in oldObjects) {\n" +
            "    scanGeometry(oldObjects[key]);\n" +
            "  }\n" +
            "\n" +
            "  newArcs = new Array(newArcsLength);\n" +
            "\n" +
            "  while (++oldIndex < oldArcsLength) {\n" +
            "    if (newIndexByOldIndex[oldIndex]) {\n" +
            "      newIndexByOldIndex[oldIndex] = ++newIndex;\n" +
            "      newArcs[newIndex] = oldArcs[oldIndex];\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  for (key in oldObjects) {\n" +
            "    newObjects[key] = reindexGeometry(oldObjects[key]);\n" +
            "  }\n" +
            "\n" +
            "  return {\n" +
            "    type: \"Topology\",\n" +
            "    bbox: topology.bbox,\n" +
            "    transform: topology.transform,\n" +
            "    objects: newObjects,\n" +
            "    arcs: newArcs\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "var filter = function(topology, filter) {\n" +
            "  var oldObjects = topology.objects,\n" +
            "      newObjects = {},\n" +
            "      key;\n" +
            "\n" +
            "  if (filter == null) filter = filterTrue;\n" +
            "\n" +
            "  function filterGeometry(input) {\n" +
            "    var output, arcs;\n" +
            "    switch (input.type) {\n" +
            "      case \"Polygon\": {\n" +
            "        arcs = filterRings(input.arcs);\n" +
            "        output = arcs ? {type: \"Polygon\", arcs: arcs} : {type: null};\n" +
            "        break;\n" +
            "      }\n" +
            "      case \"MultiPolygon\": {\n" +
            "        arcs = input.arcs.map(filterRings).filter(filterIdentity);\n" +
            "        output = arcs.length ? {type: \"MultiPolygon\", arcs: arcs} : {type: null};\n" +
            "        break;\n" +
            "      }\n" +
            "      case \"GeometryCollection\": {\n" +
            "        arcs = input.geometries.map(filterGeometry).filter(filterNotNull);\n" +
            "        output = arcs.length ? {type: \"GeometryCollection\", geometries: arcs} : {type: null};\n" +
            "        break;\n" +
            "      }\n" +
            "      default: return input;\n" +
            "    }\n" +
            "    if (input.id != null) output.id = input.id;\n" +
            "    if (input.bbox != null) output.bbox = input.bbox;\n" +
            "    if (input.properties != null) output.properties = input.properties;\n" +
            "    return output;\n" +
            "  }\n" +
            "\n" +
            "  function filterRings(arcs) {\n" +
            "    return arcs.length && filterExteriorRing(arcs[0]) // if the exterior is small, ignore any holes\n" +
            "        ? [arcs[0]].concat(arcs.slice(1).filter(filterInteriorRing))\n" +
            "        : null;\n" +
            "  }\n" +
            "\n" +
            "  function filterExteriorRing(ring) {\n" +
            "    return filter(ring, false);\n" +
            "  }\n" +
            "\n" +
            "  function filterInteriorRing(ring) {\n" +
            "    return filter(ring, true);\n" +
            "  }\n" +
            "\n" +
            "  for (key in oldObjects) {\n" +
            "    newObjects[key] = filterGeometry(oldObjects[key]);\n" +
            "  }\n" +
            "\n" +
            "  return prune({\n" +
            "    type: \"Topology\",\n" +
            "    bbox: topology.bbox,\n" +
            "    transform: topology.transform,\n" +
            "    objects: newObjects,\n" +
            "    arcs: topology.arcs\n" +
            "  });\n" +
            "};\n" +
            "\n" +
            "function filterTrue() {\n" +
            "  return true;\n" +
            "}\n" +
            "\n" +
            "function filterIdentity(x) {\n" +
            "  return x;\n" +
            "}\n" +
            "\n" +
            "function filterNotNull(geometry) {\n" +
            "  return geometry.type != null;\n" +
            "}\n" +
            "\n" +
            "var filterAttached = function(topology) {\n" +
            "  var ownerByArc = new Array(topology.arcs.length), // arc index -> index of unique associated ring, or -1 if used by multiple rings\n" +
            "      ownerIndex = 0,\n" +
            "      key;\n" +
            "\n" +
            "  function testGeometry(o) {\n" +
            "    switch (o.type) {\n" +
            "      case \"GeometryCollection\": o.geometries.forEach(testGeometry); break;\n" +
            "      case \"Polygon\": testArcs(o.arcs); break;\n" +
            "      case \"MultiPolygon\": o.arcs.forEach(testArcs); break;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  function testArcs(arcs) {\n" +
            "    for (var i = 0, n = arcs.length; i < n; ++i, ++ownerIndex) {\n" +
            "      for (var ring = arcs[i], j = 0, m = ring.length; j < m; ++j) {\n" +
            "        var arc = ring[j];\n" +
            "        if (arc < 0) arc = ~arc;\n" +
            "        var owner = ownerByArc[arc];\n" +
            "        if (owner == null) ownerByArc[arc] = ownerIndex;\n" +
            "        else if (owner !== ownerIndex) ownerByArc[arc] = -1;\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  for (key in topology.objects) {\n" +
            "    testGeometry(topology.objects[key]);\n" +
            "  }\n" +
            "\n" +
            "  return function(ring) {\n" +
            "    for (var j = 0, m = ring.length, arc; j < m; ++j) {\n" +
            "      if (ownerByArc[(arc = ring[j]) < 0 ? ~arc : arc] === -1) {\n" +
            "        return true;\n" +
            "      }\n" +
            "    }\n" +
            "    return false;\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "function planarTriangleArea(triangle) {\n" +
            "  var a = triangle[0], b = triangle[1], c = triangle[2];\n" +
            "  return Math.abs((a[0] - c[0]) * (b[1] - a[1]) - (a[0] - b[0]) * (c[1] - a[1])) / 2;\n" +
            "}\n" +
            "\n" +
            "function planarRingArea$1(ring) {\n" +
            "  var i = -1, n = ring.length, a, b = ring[n - 1], area = 0;\n" +
            "  while (++i < n) a = b, b = ring[i], area += a[0] * b[1] - a[1] * b[0];\n" +
            "  return Math.abs(area) / 2;\n" +
            "}\n" +
            "\n" +
            "var filterWeight = function(topology, minWeight, weight) {\n" +
            "  minWeight = minWeight == null ? Number.MIN_VALUE : +minWeight;\n" +
            "\n" +
            "  if (weight == null) weight = planarRingArea$1;\n" +
            "\n" +
            "  return function(ring, interior) {\n" +
            "    return weight(feature(topology, {type: \"Polygon\", arcs: [ring]}).geometry.coordinates[0], interior) >= minWeight;\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "var filterAttachedWeight = function(topology, minWeight, weight) {\n" +
            "  var a = filterAttached(topology),\n" +
            "      w = filterWeight(topology, minWeight, weight);\n" +
            "  return function(ring, interior) {\n" +
            "    return a(ring, interior) || w(ring, interior);\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "function compare(a, b) {\n" +
            "  return a[1][2] - b[1][2];\n" +
            "}\n" +
            "\n" +
            "var newHeap = function() {\n" +
            "  var heap = {},\n" +
            "      array = [],\n" +
            "      size = 0;\n" +
            "\n" +
            "  heap.push = function(object) {\n" +
            "    up(array[object._ = size] = object, size++);\n" +
            "    return size;\n" +
            "  };\n" +
            "\n" +
            "  heap.pop = function() {\n" +
            "    if (size <= 0) return;\n" +
            "    var removed = array[0], object;\n" +
            "    if (--size > 0) object = array[size], down(array[object._ = 0] = object, 0);\n" +
            "    return removed;\n" +
            "  };\n" +
            "\n" +
            "  heap.remove = function(removed) {\n" +
            "    var i = removed._, object;\n" +
            "    if (array[i] !== removed) return; // invalid request\n" +
            "    if (i !== --size) object = array[size], (compare(object, removed) < 0 ? up : down)(array[object._ = i] = object, i);\n" +
            "    return i;\n" +
            "  };\n" +
            "\n" +
            "  function up(object, i) {\n" +
            "    while (i > 0) {\n" +
            "      var j = ((i + 1) >> 1) - 1,\n" +
            "          parent = array[j];\n" +
            "      if (compare(object, parent) >= 0) break;\n" +
            "      array[parent._ = i] = parent;\n" +
            "      array[object._ = i = j] = object;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  function down(object, i) {\n" +
            "    while (true) {\n" +
            "      var r = (i + 1) << 1,\n" +
            "          l = r - 1,\n" +
            "          j = i,\n" +
            "          child = array[j];\n" +
            "      if (l < size && compare(array[l], child) < 0) child = array[j = l];\n" +
            "      if (r < size && compare(array[r], child) < 0) child = array[j = r];\n" +
            "      if (j === i) break;\n" +
            "      array[child._ = i] = child;\n" +
            "      array[object._ = i = j] = object;\n" +
            "    }\n" +
            "  }\n" +
            "\n" +
            "  return heap;\n" +
            "};\n" +
            "\n" +
            "function copy(point) {\n" +
            "  return [point[0], point[1], 0];\n" +
            "}\n" +
            "\n" +
            "var presimplify = function(topology, weight) {\n" +
            "  var point = topology.transform ? transform(topology.transform) : copy,\n" +
            "      heap = newHeap();\n" +
            "\n" +
            "  if (weight == null) weight = planarTriangleArea;\n" +
            "\n" +
            "  var arcs = topology.arcs.map(function(arc) {\n" +
            "    var triangles = [],\n" +
            "        maxWeight = 0,\n" +
            "        triangle,\n" +
            "        i,\n" +
            "        n;\n" +
            "\n" +
            "    arc = arc.map(point);\n" +
            "\n" +
            "    for (i = 1, n = arc.length - 1; i < n; ++i) {\n" +
            "      triangle = [arc[i - 1], arc[i], arc[i + 1]];\n" +
            "      triangle[1][2] = weight(triangle);\n" +
            "      triangles.push(triangle);\n" +
            "      heap.push(triangle);\n" +
            "    }\n" +
            "\n" +
            "    // Always keep the arc endpoints!\n" +
            "    arc[0][2] = arc[n][2] = Infinity;\n" +
            "\n" +
            "    for (i = 0, n = triangles.length; i < n; ++i) {\n" +
            "      triangle = triangles[i];\n" +
            "      triangle.previous = triangles[i - 1];\n" +
            "      triangle.next = triangles[i + 1];\n" +
            "    }\n" +
            "\n" +
            "    while (triangle = heap.pop()) {\n" +
            "      var previous = triangle.previous,\n" +
            "          next = triangle.next;\n" +
            "\n" +
            "      // If the weight of the current point is less than that of the previous\n" +
            "      // point to be eliminated, use the latter��s weight instead. This ensures\n" +
            "      // that the current point cannot be eliminated without eliminating\n" +
            "      // previously- eliminated points.\n" +
            "      if (triangle[1][2] < maxWeight) triangle[1][2] = maxWeight;\n" +
            "      else maxWeight = triangle[1][2];\n" +
            "\n" +
            "      if (previous) {\n" +
            "        previous.next = next;\n" +
            "        previous[2] = triangle[2];\n" +
            "        update(previous);\n" +
            "      }\n" +
            "\n" +
            "      if (next) {\n" +
            "        next.previous = previous;\n" +
            "        next[0] = triangle[0];\n" +
            "        update(next);\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    return arc;\n" +
            "  });\n" +
            "\n" +
            "  function update(triangle) {\n" +
            "    heap.remove(triangle);\n" +
            "    triangle[1][2] = weight(triangle);\n" +
            "    heap.push(triangle);\n" +
            "  }\n" +
            "\n" +
            "  return {\n" +
            "    type: \"Topology\",\n" +
            "    bbox: topology.bbox,\n" +
            "    objects: topology.objects,\n" +
            "    arcs: arcs\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "var quantile = function(topology, p) {\n" +
            "  var array = [];\n" +
            "\n" +
            "  topology.arcs.forEach(function(arc) {\n" +
            "    arc.forEach(function(point) {\n" +
            "      if (isFinite(point[2])) { // Ignore endpoints, whose weight is Infinity.\n" +
            "        array.push(point[2]);\n" +
            "      }\n" +
            "    });\n" +
            "  });\n" +
            "\n" +
            "  return array.length && quantile$1(array.sort(descending), p);\n" +
            "};\n" +
            "\n" +
            "function quantile$1(array, p) {\n" +
            "  if (!(n = array.length)) return;\n" +
            "  if ((p = +p) <= 0 || n < 2) return array[0];\n" +
            "  if (p >= 1) return array[n - 1];\n" +
            "  var n,\n" +
            "      h = (n - 1) * p,\n" +
            "      i = Math.floor(h),\n" +
            "      a = array[i],\n" +
            "      b = array[i + 1];\n" +
            "  return a + (b - a) * (h - i);\n" +
            "}\n" +
            "\n" +
            "function descending(a, b) {\n" +
            "  return b - a;\n" +
            "}\n" +
            "\n" +
            "var simplify = function(topology, minWeight) {\n" +
            "  minWeight = minWeight == null ? Number.MIN_VALUE : +minWeight;\n" +
            "\n" +
            "  // Remove points whose weight is less than the minimum weight.\n" +
            "  var arcs = topology.arcs.map(function(input) {\n" +
            "    var i = -1,\n" +
            "        j = 0,\n" +
            "        n = input.length,\n" +
            "        output = new Array(n), // pessimistic\n" +
            "        point;\n" +
            "\n" +
            "    while (++i < n) {\n" +
            "      if ((point = input[i])[2] >= minWeight) {\n" +
            "        output[j++] = [point[0], point[1]];\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    output.length = j;\n" +
            "    return output;\n" +
            "  });\n" +
            "\n" +
            "  return {\n" +
            "    type: \"Topology\",\n" +
            "    transform: topology.transform,\n" +
            "    bbox: topology.bbox,\n" +
            "    objects: topology.objects,\n" +
            "    arcs: arcs\n" +
            "  };\n" +
            "};\n" +
            "\n" +
            "var pi = Math.PI;\n" +
            "var tau = 2 * pi;\n" +
            "var quarterPi = pi / 4;\n" +
            "var radians = pi / 180;\n" +
            "var abs = Math.abs;\n" +
            "var atan2 = Math.atan2;\n" +
            "var cos = Math.cos;\n" +
            "var sin = Math.sin;\n" +
            "\n" +
            "function halfArea(ring, closed) {\n" +
            "  var i = 0,\n" +
            "      n = ring.length,\n" +
            "      sum = 0,\n" +
            "      point = ring[closed ? i++ : n - 1],\n" +
            "      lambda0, lambda1 = point[0] * radians,\n" +
            "      phi1 = (point[1] * radians) / 2 + quarterPi,\n" +
            "      cosPhi0, cosPhi1 = cos(phi1),\n" +
            "      sinPhi0, sinPhi1 = sin(phi1);\n" +
            "\n" +
            "  for (; i < n; ++i) {\n" +
            "    point = ring[i];\n" +
            "    lambda0 = lambda1, lambda1 = point[0] * radians;\n" +
            "    phi1 = (point[1] * radians) / 2 + quarterPi;\n" +
            "    cosPhi0 = cosPhi1, cosPhi1 = cos(phi1);\n" +
            "    sinPhi0 = sinPhi1, sinPhi1 = sin(phi1);\n" +
            "\n" +
            "    // Spherical excess E for a spherical triangle with vertices: south pole,\n" +
            "    // previous point, current point.  Uses a formula derived from Cagnoli��s\n" +
            "    // theorem.  See Todhunter, Spherical Trig. (1871), Sec. 103, Eq. (2).\n" +
            "    // See https://github.com/d3/d3-geo/blob/master/README.md#geoArea\n" +
            "    var dLambda = lambda1 - lambda0,\n" +
            "        sdLambda = dLambda >= 0 ? 1 : -1,\n" +
            "        adLambda = sdLambda * dLambda,\n" +
            "        k = sinPhi0 * sinPhi1,\n" +
            "        u = cosPhi0 * cosPhi1 + k * cos(adLambda),\n" +
            "        v = k * sdLambda * sin(adLambda);\n" +
            "    sum += atan2(v, u);\n" +
            "  }\n" +
            "\n" +
            "  return sum;\n" +
            "}\n" +
            "\n" +
            "function sphericalRingArea(ring, interior) {\n" +
            "  var sum = halfArea(ring, true);\n" +
            "  if (interior) sum *= -1;\n" +
            "  return (sum < 0 ? tau + sum : sum) * 2;\n" +
            "}\n" +
            "\n" +
            "function sphericalTriangleArea(t) {\n" +
            "  return abs(halfArea(t, false)) * 2;\n" +
            "}\n" +
            "\n" +
            "exports.bbox = bbox;\n" +
            "exports.feature = feature;\n" +
            "exports.mesh = mesh;\n" +
            "exports.meshArcs = meshArcs;\n" +
            "exports.merge = merge;\n" +
            "exports.mergeArcs = mergeArcs;\n" +
            "exports.neighbors = neighbors;\n" +
            "exports.quantize = quantize;\n" +
            "exports.transform = transform;\n" +
            "exports.untransform = untransform;\n" +
            "exports.topology = topology;\n" +
            "exports.filter = filter;\n" +
            "exports.filterAttached = filterAttached;\n" +
            "exports.filterAttachedWeight = filterAttachedWeight;\n" +
            "exports.filterWeight = filterWeight;\n" +
            "exports.planarRingArea = planarRingArea$1;\n" +
            "exports.planarTriangleArea = planarTriangleArea;\n" +
            "exports.presimplify = presimplify;\n" +
            "exports.quantile = quantile;\n" +
            "exports.simplify = simplify;\n" +
            "exports.sphericalRingArea = sphericalRingArea;\n" +
            "exports.sphericalTriangleArea = sphericalTriangleArea;\n" +
            "\n" +
            "Object.defineProperty(exports, '__esModule', { value: true });\n" +
            "\n" +
            "})));";

    private String main = "function geo2topo(data, quantization){\n" +
            "    var objects = JSON.parse(data);\n" +
            "    var topoObject = topojson.topology(objects, quantization);\n" +
            "    return JSON.stringify(topoObject);\n" +
            "}\n" +
            "\n" +
            "function topo2geo(topo, foo){\n" +
            "    var topoObjects = JSON.parse(topo);\n" +
            "    var fooObjects = JSON.parse(foo);\n" +
            "    var geoObject = topojson.feature(topoObjects, fooObjects);\n" +
            "    return JSON.stringify(geoObject);\n" +
            "}";
}

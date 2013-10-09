// OMIM result list template
var results_item_markup = '<li><a data-id="\${omim}" href="#\${omim}" class="opensearch"><i class="icon-chevron-right"></i>\${omim}</a></li>';
/**
 * Search results loading.
 *
 * // To Do
 *  - take this out of here!
 *
 * @param {type} id
 * @returns {undefined}
 */

function loadResults(id) {
	var uri = '';
	if (window.location.toString().indexOf('id') > 0) {
		uri = encodeURI(path + '/services/results/id/' + id);
	} else {
		uri = encodeURI(path + '/services/results/full/' + id);
	}
	$.getJSON(uri, function(data) {
		if (data.status === 110) {
			$('#loading').fadeOut().remove();
			$('#errors').fadeIn(1000);
			$('#alert_noresults').fadeIn(1000);
			toggleTopButton('mag');
			setTimeout(function() {
				$('#text_search').focus();
			}, 400);
		} else if (data.status === 121) {
			window.location = path + '/entry/' + data.results[0].omim;
		} else if (data.status === 140) {
			$('#loading').fadeOut().remove();
			$('#errors').fadeIn(1000);
			$('#alert_short').fadeIn(1000);
			toggleTopButton('mag');
			setTimeout(function() {
				$('#text_search').focus();
			}, 400);
		} else {
			$('#results_size').html(data.size);
			data.results.sort(function(a,b) {
				return (a.omim == b.omim) ? 0 : (a.omim > b.omim) ? 1 : -1;
			});
			$.tmpl('results', data.results).appendTo('#results_list');
			$.each(data.results, function(i, value) {
				var box = $('<div/>', {
					'class': 'results_list well well-sm',
					'data-omim': value.omim,
					'id': value.omim
				});
				box.append('<div class="col-md-12 results_title clear"><i class="icon-bookmark-empty"></i> <a href="' + path + '/entry/' + value.omim + '" rel="tooltip" data-placement="bottom" title="View ' + value.name + ' in Diseasecard">' + value.omim + ' - ' + value.name + '</a><span class="pull-right external"><a rel="tooltip" title="Open ' + value.omim + ' in OMIM" data-placement="bottom" href="http://omim.org/entry/' + value.omim + '" target="_blank"><i class="icon-external-link"></i></a></span></div>')
				$.each(value.links, function(j, link) {
					var dlink = $('<ul/>', {
						'class': 'results_items',
						'data-id': link
					}).append('<li><i class="icon-angle-right"></i><a rel="tooltip" title="View ' + link.substring(7, link.length) + ' for ' + value.omim + ' in Diseasecard" href="' + path + '/entry/' + value.omim + '#' + link.substring(7, link.length) + '"> ' + link.substring(7, link.length) + '</a></li>');
					box.append(dlink);
				})
				$('#results_links').append(box);
			})
			$('#loading').fadeOut(500).remove();
			if (uri.indexOf('id') > 0) {
				$('#results_search').remove();
				$('#results_links').removeClass('col-md-9').addClass('col-md-12');
				$('#meta').fadeIn(800);
				$('#results').fadeIn(800);
				$('#results_links').height($('html').height() - 110).css('padding','0');
				$('.results_list').width($('#meta').width() - 48);
				$('#results').width($('#meta').width());
			} else {
				$('#meta').fadeIn(800);
				$('#results_list').fadeIn(800);
				$('#results').fadeIn(800);
				$('#results_search').height($('html').height() - 110);
				$('#results_links').height($('html').height() - 110);

				$('#results').width($('#meta').width());
			}
			var tour = new Tour({
				name: "diseasecard_search_tour",
				keyboard: true
			});
			tour.addStep({
				animation: true,
				placement: 'right',
				element: "#results_search",
				title: "Identifiers",
				content: "Browse the results for the OMIM identifiers associated with your <strong>" + id + "</strong> query<br />Identifiers are ordered by <strong>relevance</strong><br/>"
			});
			tour.addStep({
				animation: true,
				placement: 'left',
				element: "#results_links",
				title: "Full results",
				content: "View and access all the pages where <strong>" + id + "</strong> was found<br/>"
			});
			tour.addStep({
				animation: true,
				placement: 'bottom',
				element: "#tour_filter",
				title: "Filter",
				content: "Reduce the result set by adding new <strong>filtering</strong> criteria<br/>"
			});
			tour.start();
			$('[rel=tooltip]').tooltip();
			$('#filter').focus();
		}
	});
}
$(document).ready(function() {
	// event handler to fix components sizes on resize
	$(window).resize(function() {
		if (this.resizeTO) clearTimeout(this.resizeTO);
		this.resizeTO = setTimeout(function() {
			$(this).trigger('resizeEnd');
		}, 50);
	});
	$(window).bind('resizeEnd', function() {
		$('#results_search').height($('html').height() - 110);
		$('#results_links').height($('html').height() - 110);
		$('#results').width($('#meta').width());
	});
	// page content filter
	$(document).on('keyup', '#filter', function() {
		var value = $('#filter').val();
		if (value.length >= 3) {
			$('.results_list').each(function() {
				if ($(this).html().toLowerCase().indexOf(value.toLowerCase()) === -1) {
					$(this).hide();
				}
			});
		} else {
			$('.results_list').show();
		}
	});
	$.template('results', results_item_markup);
	$('.mag').click(function() {
		showSearch($(this));
		setTimeout(function() {
			$('#text_search').focus();
		}, 400);
	});
});
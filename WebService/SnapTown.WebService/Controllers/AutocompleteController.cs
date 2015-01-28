using SnapTown.DAL;
using SnapTown.DTO;
using SnapTown.Models;
using SnapTown.WebService.Converters;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace SnapTown.WebService.Controllers
{
    [RoutePrefix("api/autocomplete")]
    public class AutocompleteController : ApiController
    {
        private IUnitOfWork unitOfWork { get; set; }
        public AutocompleteController(IUnitOfWork unitOfWork)
        {
            this.unitOfWork = unitOfWork;
        }

        [Route("towns/{query}")]
        [HttpGet]
        public IQueryable<TownDto> Autocomplete(string query)
        {
            return this.unitOfWork.Towns.Filter(t => t.Name.StartsWith(query),
                new string[] { "Country" }).Select(TownConverter.AsTownDto);
        }
    }
}

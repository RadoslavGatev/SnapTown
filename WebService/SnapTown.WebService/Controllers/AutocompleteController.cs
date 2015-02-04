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
using System.Web.Http.OData;

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
        [EnableQuery]
        public IQueryable<TownDto> Autocomplete(string query, string authToken)
        {
            var user = unitOfWork.Users.Get(x => x.AuthToken == authToken);
            return this.unitOfWork.Towns.Filter(t => t.Name.StartsWith(query),
                new string[] { "Country" })
            .Select(town => new TownDto()
            {
                TownID = town.TownID,
                Country = town.Country.CountryCode,
                Name = town.Name,
                IsSubscribed = town.Subscriptions.Any(u => u.UserID == user.UserID)
            });
        }
    }
}

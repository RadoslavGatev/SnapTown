using SnapTown.DTO;
using SnapTown.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace SnapTown.WebService.Converters
{
    public static class TownConverter
    {
        public static readonly Expression<Func<Town, TownDto>> AsTownDto =
              x => new TownDto
              {
                  TownID = x.TownID,
                  Name = x.Name,
                  Country = x.Country.CountryCode
              };

        public static readonly Expression<Func<Town, TownDto>> AsSubscribedTownDto =
             x => new TownDto
             {
                 TownID = x.TownID,
                 Name = x.Name,
                 Country = x.Country.CountryCode,
                 IsSubscribed = true
             };
    }
}